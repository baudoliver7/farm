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
package com.zerocracy.tk.project.reports;

import com.jcabi.xml.XML;
import com.zerocracy.farm.props.PropsFarm;
import com.zerocracy.jstk.Project;
import com.zerocracy.jstk.farm.fake.FkProject;
import com.zerocracy.pm.ClaimOut;
import com.zerocracy.pm.Claims;
import com.zerocracy.pm.Footprint;
import java.util.Date;
import org.bson.Document;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link OrderChampions}.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.18
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class OrderChampionsTest {

    @Test
    public void retrievesReportData() throws Exception {
        final Project pkt = new FkProject("7460A2829");
        new ClaimOut()
            .type("Order was given")
            .param("login", "yegor256")
            .postTo(pkt);
        final XML xml = new Claims(pkt).iterate().iterator().next();
        try (final Footprint footprint = new Footprint(new PropsFarm(), pkt)) {
            footprint.open(xml);
            final Iterable<Document> docs = footprint.collection().aggregate(
                new OrderChampions().bson(
                    pkt,
                    new Date(0L),
                    new Date()
                )
            );
            MatcherAssert.assertThat(docs, Matchers.iterableWithSize(1));
            MatcherAssert.assertThat(
                docs.iterator().next().get("user"),
                Matchers.equalTo("@yegor256")
            );
        }
    }

}