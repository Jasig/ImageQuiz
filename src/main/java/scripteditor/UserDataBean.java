/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package scripteditor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Anurag
 */
public class UserDataBean implements Serializable{
    private String taxonomicLevel; // This is the level that will be studied.
    private String[] mTaxa; // These are the taxa that will be studied.
    private String selectBy;
   
    // exctra information for group data 
    private boolean  selCname;
    private ArrayList sel=new ArrayList();//used for storing the selected image names from group selection box
    private boolean group;
    private boolean taxanull;
    private boolean selctfamilyTaxanull;
    
    public String getTaxonomicLevel() {
        return taxonomicLevel;
    }

    public void setTaxonomicLevel(String taxonomicLevel) {
        this.taxonomicLevel = taxonomicLevel;
    }

    public String[] getmTaxa() {
        return mTaxa;
    }

    public void setmTaxa(String[] mTaxa) {
        this.mTaxa = mTaxa;
    }

    public String getSelectBy() {
        return selectBy;
    }

    public void setSelectBy(String selectBy) {
        this.selectBy = selectBy;
    }

    public boolean isSelCname() {
        return selCname;
    }

    public void setSelCname(boolean selCname) {
        this.selCname = selCname;
    }

    public ArrayList getSel() {
        return sel;
    }

    public void setSel(ArrayList sel) {
        this.sel = sel;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public boolean isTaxanull() {
        return taxanull;
    }

    public void setTaxanull(boolean taxanull) {
        this.taxanull = taxanull;
    }

    public boolean isSelctfamilyTaxanull() {
        return selctfamilyTaxanull;
    }

    public void setSelctfamilyTaxanull(boolean selctfamilyTaxanull) {
        this.selctfamilyTaxanull = selctfamilyTaxanull;
    }
    
}
