/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.core.updater;

import static fr.norad.core.updater.Version.V;
import static org.fest.assertions.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.Test;

public class ApplicationUpdaterTest {

    class TestUpdate extends Update {
        public TestUpdate(String name) {
            super(name);
        }

        public boolean updated;

        @Override
        public void runUpdate() {
            updated = true;
        }
    }

    class TestApplicationUpdater extends ApplicationUpdater {
        public Version version = null;

        public TestApplicationUpdater(ApplicationVersion... updates) {
            super("test", new HashSet<>(Arrays.asList(updates)));
        }

        @Override
        protected void setNewVersion(Version version) {
            this.version = version;
        }

        @Override
        protected Version getCurrentVersion() {
            return version;
        }
    }

    @Test
    public void should_not_have_to_update() throws Exception {
        TestUpdate update = new TestUpdate("updateA");
        TestApplicationUpdater updater = new TestApplicationUpdater(new ApplicationVersion(V(1), update));
        updater.version = V(42);

        Version newVersion = updater.updateFromOneVersion();

        assertThat(newVersion).isNull();
        assertThat(update.updated).isFalse();
        assertThat(updater.version).isEqualTo(V(42));
    }

    @Test
    public void should_update() throws Exception {
        TestUpdate update = new TestUpdate("updateA");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(V(1), update));

        Version newVersion = runner.updateFromOneVersion();

        assertThat(newVersion).isEqualTo(V(1));
        assertThat(update.updated).isTrue();
        assertThat(runner.version).isEqualTo(V(1));
    }

    @Test
    public void should_find_good_update() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(V(0, 2), update02), //
                new ApplicationVersion(V(1), update1));
        runner.version = V(0, 2);

        assertThat(runner.updateFromOneVersion()).isEqualTo(V(1));
        assertThat(update02.updated).isFalse();
        assertThat(update1.updated).isTrue();
        assertThat(runner.version).isEqualTo(V(1));
    }

    @Test
    public void should_find_good_update2() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(V(0, 2), update02), //
                new ApplicationVersion(V(1), update1));
        runner.version = V(0, 3);

        assertThat(runner.updateFromOneVersion()).isEqualTo(V(1));
        assertThat(update02.updated).isFalse();
        assertThat(update1.updated).isTrue();
        assertThat(runner.version).isEqualTo(V(1));
    }

    @Test
    public void should_update_to_latest() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(V(1), update1), //
                new ApplicationVersion(V(0, 2), update02));

        runner.updateToLatest();

        assertThat(update02.updated).isTrue();
        assertThat(update1.updated).isTrue();
        assertThat(runner.version).isEqualTo(V(1));
    }

    @Test
    public void should_update_to_specific_version() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestUpdate update2 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(V(1), update1), //
                new ApplicationVersion(V(0, 2), update02), //
                new ApplicationVersion(V(2), update2) //
        );

        runner.updateTo(V(1));

        assertThat(update02.updated).isTrue();
        assertThat(update1.updated).isTrue();
        assertThat(update2.updated).isFalse();
        assertThat(runner.version).isEqualTo(V(1));
    }

    @Test
    public void should_update_to_specific_version3() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestUpdate update2 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(V(1), update1), //
                new ApplicationVersion(V(0, 2), update02), //
                new ApplicationVersion(V(2), update2) //
        );

        runner.updateTo(V(0, 9));

        assertThat(update02.updated).isTrue();
        assertThat(update1.updated).isFalse();
        assertThat(update2.updated).isFalse();
        assertThat(runner.version).isEqualTo(V(0, 2));
    }

    @Test
    public void should_update_to_specific_version2() throws Exception {
        TestUpdate update02 = new TestUpdate("update");
        TestUpdate update1 = new TestUpdate("update");
        TestUpdate update2 = new TestUpdate("update");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(V(1), update1), //
                new ApplicationVersion(V(0, 2), update02), //
                new ApplicationVersion(V(2), update2) //
        );
        runner.version = V(0, 3);

        runner.updateTo(V(1, 5));

        assertThat(update02.updated).isFalse();
        assertThat(update1.updated).isTrue();
        assertThat(update2.updated).isFalse();
        assertThat(runner.version).isEqualTo(V(1));
    }

    @Test
    public void should_run_multiple_update() throws Exception {
        TestUpdate update = new TestUpdate("update");
        TestUpdate updateA = new TestUpdate("updateA");
        TestApplicationUpdater runner = new TestApplicationUpdater(new ApplicationVersion(V(1), update, updateA));

        Version newVersion = runner.updateFromOneVersion();

        assertThat(newVersion).isEqualTo(V(1));
        assertThat(update.updated).isTrue();
        assertThat(updateA.updated).isTrue();
        assertThat(runner.version).isEqualTo(V(1));
    }

}
