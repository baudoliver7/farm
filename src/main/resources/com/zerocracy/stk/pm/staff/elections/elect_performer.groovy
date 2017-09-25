/**
 * Copyright (c) 2016-2017 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.stk.pm.staff.elections

import com.jcabi.log.Logger
import com.jcabi.xml.XML
import com.zerocracy.farm.Assume
import com.zerocracy.jstk.Farm
import com.zerocracy.jstk.Project
import com.zerocracy.pm.ClaimOut
import com.zerocracy.pm.scope.Wbs
import com.zerocracy.pm.staff.Elections
import com.zerocracy.pm.staff.Roles
import com.zerocracy.pm.staff.voters.Banned
import com.zerocracy.pm.staff.voters.NoRoom
import com.zerocracy.pm.staff.voters.Vacation
import com.zerocracy.pmo.Pmo
import com.zerocracy.pm.staff.voters.Workload

def exec(Project project, XML xml) {
  new Assume(project, xml).type('Ping')
  new Assume(project, xml).notPmo()
  Wbs wbs = new Wbs(project).bootstrap()
  Roles roles = new Roles(project).bootstrap()
  List<String> logins = roles.findByRole('DEV')
  if (logins.empty) {
    Logger.info(this, 'No DEVs in %s, cannot elect', project)
    return
  }
  Elections elections = new Elections(project).bootstrap()
  Set<String> winners = new HashSet<>()
  Farm farm = binding.variables.farm
  Project pmo = new Pmo(farm)
  for (String job : wbs.iterate()) {
    elections.elect(
      job, logins,
      [
        (new NoRoom(pmo)): -100,
        (new Banned(project, job)): -1000,
        (new Vacation(project)): -1000,
        (new Workload(pmo)): 1
      ]
    )
    if (elections.elected(job)) {
      String winner = elections.winner(job)
      if (!winners.contains(winner)) {
        winners.add(winner)
        new ClaimOut()
          .type('Performer was elected')
          .param('login', winner)
          .param('job', job)
          .param('reason', elections.reason(job))
          .postTo(project)
      }
    }
  }
}
