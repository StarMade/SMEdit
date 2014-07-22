/**
 * Copyright 2014 
 * SMEdit https://github.com/StarMade/SMEdit
 * SMTools https://github.com/StarMade/SMTools
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/
package jo.sm.mods;

/**
 * @Auther Jo Jaquinta for SMEdit Classic - version 1.0
 **/
public interface IPluginCallback {

    public void setStatus(String status);

    public void startTask(int size);

    public void workTask(int amnt);

    public void endTask();

    public boolean isPleaseCancel();

    public void setErrorTitle(String title);

    public void setErrorDescription(String desc);

    public void setError(Throwable t);
}
