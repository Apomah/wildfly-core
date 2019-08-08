/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2019 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.extension.elytron.capabilities._private;

import java.util.function.Consumer;

import org.wildfly.common.Assert;
import org.wildfly.security.auth.server.event.RealmEvent;

/**
 * A {@link Consumer} that consumes {@link RealmEvent} instances emmitted from domains.
 *
 * @author <a href="mailto:jucook@redhat.com">Justin Cook</a>
 */
public interface RealmEventListener extends Consumer<RealmEvent> {

    static RealmEventListener from(Consumer<RealmEvent> consumer) {
        return consumer::accept;
    }

    static RealmEventListener aggregate(RealmEventListener... listeners) {
        Assert.checkNotNullParam("listeners", listeners);
        final RealmEventListener[] clone = listeners.clone();
        for (int i = 0; i < clone.length; i++) {
            Assert.checkNotNullArrayParam("listener", i, clone[i]);
        }
        return e -> {
            for (RealmEventListener sel : clone) {
                sel.accept(e);
            }
        };
    }

}
