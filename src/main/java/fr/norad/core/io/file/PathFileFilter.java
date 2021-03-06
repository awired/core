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
package fr.norad.core.io.file;

import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

public class PathFileFilter {

    private final Map<String, FileFilter> filterMap = new HashMap<String, FileFilter>();
    private FileFilter                    globalFilter;

    /**
     * 
     * TODO create a superfilter containing all filter (using a filter chain) and return it.
     * 
     * @param path
     * @return
     */
    public FileFilter getFilterFromPath(String path) {
        return globalFilter;
    }

    /**
     * @param globalFilter
     *            the globalFilter to set
     */
    public void setGlobalFilter(FileFilter globalFilter) {
        this.globalFilter = globalFilter;
    }
}
