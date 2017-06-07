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
package com.zerocracy.stk.pm.in.orders

import com.jcabi.xml.XML
import com.zerocracy.jstk.Project
import com.zerocracy.pm.ClaimIn
import com.zerocracy.pm.ClaimOut
import com.zerocracy.pm.in.Orders

def exec(Project project, XML xml) {
  assume.type('Start order').exact()
  assume.roles('ARC', 'PO').exist()

  ClaimIn claim = new ClaimIn(xml)
  String job = claim.param('job')
  String login = claim.param('login')
  if ('me' == login) {
    login = claim.author()
  }
  new Orders(project).bootstrap().assign(job, login)
  claim.reply(
    String.format(
      'Job `%s` assigned to @%s, please go ahead.',
      job, login
    )
  ).postTo(project)
  new ClaimOut(
    new ClaimOut.ToUser(
      project,
      login,
      String.format(
        'Job `%s` was assigned to you a minute ago.',
        job
      )
    )
  ).postTo(project)
  new ClaimOut(
    new ClaimOut.ToProject(
      project,
      String.format(
        // @checkstyle LineLength (1 line)
        'Job `%s` was assigned to [@%s](https://github.com/%1$s).',
        job, login
      )
    )
  ).postTo(project)
}
