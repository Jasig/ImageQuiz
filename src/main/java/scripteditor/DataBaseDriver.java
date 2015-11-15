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
/*
 * DataBaseDriver.java
 *
 * Created on June 13, 2006, 5:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scripteditor;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
import java.io.*;

/**
 *
 * @author Moz123
 */
public class DataBaseDriver {

    public static final String DATABASE_FOLDER = "Database";
    public static final String FIELD_RELATIONSHIPS_FILE = "FieldRelationships.csv";
    public static final int FIELD_RELATIONSHIPS_OFFSET = 0;

    String[] mFamily;
    String[] mGenus;
    //Added by preethy on 7-01-2012
    
    ArrayList subject = new ArrayList();
    ArrayList group = new ArrayList();
    ArrayList origin = new ArrayList();
    ArrayList form = new ArrayList();
    ArrayList use = new ArrayList();
    ArrayList phototype = new ArrayList();
    ArrayList selectedFilters = new ArrayList();
    ArrayList selectedRow = new ArrayList();
    ArrayList fm = new ArrayList();
    ArrayList gen = new ArrayList();
       
    Boolean sub=false,org=false,grp=false,frm=false,use1=false,ph_type=false;//these flags are used in search for checking which one is selected
    ArrayList flagExtra = new ArrayList();
    Boolean selctedsub=false,selectedorg=false,selectedgrp=false,selectedform=false,selecteduse=false,selectedph_type=false;
    ArrayList selectedExtra = new ArrayList();
    String[] taxaList; 
    //**//
    ArrayList imageFileNames = new ArrayList();        //hold image file names
    ArrayList taxonomy = new ArrayList();   //hold taxonomic names
    public boolean withSubspecies;
    private String mDataBaseName;
    public boolean cname=false; //for checking common name
    public boolean isFromTexaSelect=false;

    // Fields for GetFieldRelationships function. By having fields at the main
    // class level for this function, we can run it just once each time the group
    // selection window is opened, and then use the values for the entire session
    // of selecting groups. Then, if the group selection window is closed and re-
    // opened, the values will be repopulated, allowing someone to fix errors in
    // the file without closing and re-opening the program.
    
    public ArrayList fieldRelationshipSets, fieldRelationshipMasterFields,
            fieldRelationshipDepFields, fieldRelationshipReference;

    /** Creates a new instance of DataBaseDriver */
    public DataBaseDriver(String dataBaseName) {
        mDataBaseName = dataBaseName;
        withSubspecies = false;
        fieldRelationshipSets = null;
    }

    public boolean DataBaseExists() {
        //TODO: remove commented code.
        //String currentDirectory = Configuration.ApplicationPath();
        String path = this.DatabasePath();
       
        File checkFile = new File(path);
        boolean retVal = checkFile.exists();
        checkFile = null;
        return retVal;
    }
    
    public String[] getTaxa(String level) {
        int idx = GetIndex(level);
      
        BufferedReader buffer = myFileReader();
       
        SortedSet taxaList = GetList(buffer, level, idx, -1);
        CloseBuffer(buffer);
        
        return treeToList(taxaList);
    }

    //Added by preethy on 20-04-2012;
    public String[] getTaxa3(String level) {
        int idx = GetIndex2(level);
         
        BufferedReader buffer = myFileReader();
       
        SortedSet taxaList = GetList(buffer, level, idx, -1);
        CloseBuffer(buffer);
        
        return treeToList(taxaList);
    }

    public String[] getGenus(String family) {

        BufferedReader buffer = myFileReader();
        SortedSet genusList = GetList(buffer, family, GetIndex("Genus"), GetIndex("Family"));
        CloseBuffer(buffer);
        return treeToList(genusList);
    }

//    SUBSPECIES NOT USED IN Missouri PROJECT
//    public String[] getSubspecies(String species){
//
//        BufferedReader buffer = myFileReader();
//        SortedSet SubspeciesList = GetList(buffer, species, GetIndex("Subspecies"), GetIndex("Species"));
//        CloseBuffer(buffer);        
//        return treeToList(SubspeciesList);
//    }

    public String[] getCommonName(String genus)  {
        BufferedReader buffer = myFileReader();
        SortedSet speciesList = GetList(buffer, genus, GetIndex("Common Name"), GetIndex("Genus"));
        CloseBuffer(buffer);
        return treeToList(speciesList);
    }

    public String[] getSpecies(String genus) {

        BufferedReader buffer = myFileReader();
        SortedSet speciesList = GetList(buffer, genus, GetIndex("Species"), GetIndex("Genus"));
        CloseBuffer(buffer);
        return treeToList(speciesList);
    }
       
    public String[][] getFileNames(String level, String[] taxa, boolean setEqual, int max) {
        // Open the CSV file containing taxa names and image file names for each.
        // Gets all the image file names for the taxa in String[] taxa.
        //    level     {Family, Genus, Species, Common Names}
        //    max       the total number of images allowed.
        //    taxa[]    The taxas from which to select images.
        // if max = 0, ignore.
        // If setEqual == true, all taxa will contain the same number of images.
        // GetIndex() returns the index corresponding to 'level'

        // BUG (TOO MANY TAXA SMALLER IMAGE SET) FIX
        if (taxa.length > max && max > 0) {
            String[] taxaTrimmed = new String[max];
            for (int i = 0; i < max; i++) {
                taxaTrimmed[i] = taxa[i];
            }
            taxa = taxaTrimmed;
        }
     

        ///////////////////////////////////////////////////////
        int idx = GetIndex(level);
        imageFileNames = new ArrayList();        //hold image file nameas
        taxonomy = new ArrayList();   //hold taxonomic names
        BufferedReader buffer = myFileReader(); //Returns file reader
        String line;
        String s;
        Arrays.sort(taxa);
        //Reader header and do nothing
        try {
            line = buffer.readLine();
        } catch (IOException ec) {
            //TODO:  Something.
        }
        int[] numImagesPerTaxa = new int[taxa.length];
        
        while (true) {
            s = "";
            try {
                //Read next taxa-name string.
                //If valid, split it into famiy, genus, species, subspieces as
                //necessary.
                line = buffer.readLine();
                if (line == null) {
                    break;
                } else {
                    line = line.trim();
                }
                String[] rec = line.split(",");
                s = GetTaxaString(rec, idx);
               
                for (int j = 0; j < taxa.length; j++) {
                     
                    if (s.compareTo(taxa[j]) == 0) {
                        numImagesPerTaxa[j]++;
                        //search index to insert item so list is alphabetized
                        //insert item at index k.
                        int k = 0;
                        while(true) {
                            if (taxonomy.size() == 0) {//Empty collection.
                                //first item here.
                                taxonomy.add(s);
                                imageFileNames.add(rec[0]);
                                break;
                            } else if (k == taxonomy.size()) {// Not found...
                                taxonomy.add(s);            // Insert at end.
                                imageFileNames.add(rec[0]);
                                break;
                            } else if (taxonomy.get(k).toString().compareTo(s) > 0) {
                                //  Perform insertion as index k
                                taxonomy.add(k, s);
                                imageFileNames.add(k, rec[0]);
                                break;
                            } else {
                                k++;
                            }
                        }
                    }
                }
            } catch (IOException ec) {
                break;
            }
           
        }
        CloseBuffer(buffer);
     
        if (max > 0) {
            TrimRecords(numImagesPerTaxa, imageFileNames, max);
        }
        if (setEqual == true) {
            EqualRecords(numImagesPerTaxa, taxa);
        }
       
        return AddFileExtension(imageFileNames, taxonomy);
    }

    private void CloseBuffer(BufferedReader buffer) {
        try {
            buffer.close();
        } catch (IOException ec) {
            
        } catch (NullPointerException ec){
            
        }
    }

    private void EqualRecords(int[] imgsPerTaxa, String[] taxa) {
        //Cull the list so each has same number of records.
        int smallest = GetSmallest(imgsPerTaxa);
        int first = 0;
        int last = 0;
        ArrayList tempAL = new ArrayList();
        ArrayList tempID = new ArrayList();
        for (int i = 0; i < taxa.length; i++) {
            String cur = "";
            try {
                cur = taxonomy.get(first).toString();
            } catch (Exception ec) {
            }
            last = first;
            while (last < taxonomy.size()) {
                if (taxonomy.get(last).toString().compareTo(cur) != 0) {
                    break;
                }
                last++;
            }
            int[] randNums = new int[last - first];
            for (int t = 0; t < (last - first); t++) {
                randNums[t] = t + first;
            }
            RandomizeArray(randNums);
            //Extract the chosen ones.
            for (int u = 0; u < smallest; u++) {
                tempAL.add(taxonomy.get(randNums[u]).toString());
                tempID.add(imageFileNames.get(randNums[u]).toString());
            }
            first = last;
        }
        taxonomy.clear();
        imageFileNames.clear();
        for (int i = 0; i < tempAL.size(); i++) {
            taxonomy.add(tempAL.get(i));
            imageFileNames.add(tempID.get(i));
        }
    }

    private SortedSet GetList(BufferedReader buffer, String name, int idx, int parent) {
        String line = "";
        SortedSet lst = new TreeSet();
                
        //Reader header and do nothing
        try {
            line = buffer.readLine();
           
        } catch (IOException ec) {
            
        }
        String sep = ",";

        //Skip through all < Family name.
        try {
            line = buffer.readLine();
            
        } catch (IOException ec) {
        }
        if (line != null) {
            line = line.trim();
        }
         
        while (line != null) {
                
              //Extract all the ones where family matches
            try {
                
               //Added by preethy
                if(line.length()-1 == line.lastIndexOf(","))
                {
                line = line+"nill";
                }
                 /***/               
                String[] rec = line.split(sep);
                              
                if (parent >= 0) {
                    lst = Extract(lst, rec, name, parent, idx);
                } else {
                    lst = Extract(lst, rec, idx);
                }
                
            } catch (PatternSyntaxException ec) {
                break;
            } catch (Exception ec) {
                break;
            }
            try {
                line = buffer.readLine();
                
            } catch (IOException ec) {
                break;
            }
            if (line != null) {
                line = line.trim();
            }
        }
        
        return lst;
    }

    private int GetSmallest(int[] arr) {
        int smallest = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < smallest) {
                smallest = arr[i];
            }
        }
        return smallest;
    }

    private String GetTaxaString(String[] record, int idx) {
       
        if (idx == 4) {                //family
            return record[4];
        } else if (idx == 2) {         //genus
            return record[4] + " " + record[2];
            // return record[2];
        } else if (idx == 3) {         //species
            return record[4] + " " + record[2] + " " + record[3];
            //Added by preethy on 09-01-2012
        } else if (idx == 5) {         //origin
            return record[5];
        } else if (idx == 6) {         //form
            return record[6];
        } else if (idx == 7) {         //use
            return record[7];
        } else if (idx == 8) {         //subject
            return record[8];
        } else if (idx == 9) {         //Group
            return record[9];
        } else if (idx == 10) {         //photo-type
            return record[10];
        } else if (idx == -15) {         //common -name//search
            return record[1];
        }else if (idx == -16) {         //common -name2//search
            return record[11];
        }else if (idx == -17) {         //common -name3//search
            return record[12];
        }else if (idx == -18) {         //common -name//search
            return record[13];
        }
                
                
          /**/      
        else {         //subspecies
            //return record[4] + " " + record[2] + " " + record[3] + " ssp. " + record[1];

            //For Kirchoff project Missouri B.G.
            //  Return Family + ' ' + Common Name
           
            if(cname)
            {
               
                 return record[4] + " " + record[1]+"#"+record[2] + " " + record[3];
            }
            else
            return record[4] + " " + record[1];
            
        }
    }
   // private String GetTaxaString2(String[] record, int idx) {
        
  // }
    

    private SortedSet Extract(SortedSet lst, String[] rec, String name, int parent, int idx) {

        if (rec[parent].compareTo(name) == 0) {
            String[] recArray = parseMultiSelect(rec[idx]); // multiselect vals are semicolon-delimited
            if (withSubspecies) {
                if (rec[1].length() > 0) //check subspecies exists
                {
                    for (String recArray1 : recArray) {
                        lst.add(recArray1);
                    }
                }
            } else {
                for (String recArray1 : recArray) {
                    lst.add(recArray1);
                }
            }
        }
        
        return lst;

    }

    private SortedSet Extract(SortedSet lst, String[] rec, int idx) {
      
        if (withSubspecies) {
            if (rec[1].length() > 0) //check subspecies exists
            {
                String[] recArray = parseMultiSelect(rec[idx]); // multiselect vals are semicolon-delimited
                for (String recArray1 : recArray) {
                    if (recArray1 != null && !recArray1.toString().equals("nill") && !recArray1.equals("")) {
                        //Added by preethy
                        lst.add(recArray1);
                    }
                }
               
            }
        } else {
            
            //lst.add(rec[idx]);}//Commented by preethy
           //Added by preethy
            String[] recArray = parseMultiSelect(rec[idx]); // multiselect vals are semicolon-delimited
            for (String recArray1 : recArray) {
                if (recArray1 != null && !recArray1.toString().equals("nill") && !recArray1.equals("")) {
                    //Added by preethy
                    lst.add(recArray1);
                }
            }
        }
      
        return lst;
    }

    private int GetIndex(String level) {
       
        if (level.compareTo("Subspecies") == 0 || level.compareTo("Common Name") == 0)// or "common names"
        {
            return 1;
        } else if (level.compareTo("Genus") == 0) {
            return 2;
        } else if (level.compareTo("Species") == 0) {
            return 3;
        } else if (level.compareTo("Family") == 0) {
            return 4; //Family
            //Added by preethy on 05-01-2012
        } else if (level.compareTo("Origin") == 0) {
            return 5; 
        } else if (level.compareTo("Form") == 0) {
            return 6; 
        }else if (level.compareTo("Use") == 0) {
            return 7; 
        }else if (level.compareTo("Subject") == 0) {
            return 8; 
        }else if (level.compareTo("Group") == 0) {
            return 9; 
        }else if (level.compareTo("Photo-Type") == 0) {
            return 10; 
        }
        else if (level.compareTo("Common Name2") == 0) {
            return 11; 
        }
          else if (level.compareTo("Common Name3") == 0) {
            return 12; 
        }     
            else if (level.compareTo("Common Name4") == 0) {
            return 13; 
        }
           /***/
        else {
            return - 1;
        }
    }
     public int GetIndex2(String level) { //This funcion is used to return the index.based on the table headers 
         String line = "";
        ArrayList header = new ArrayList();
         BufferedReader buffer = myFileReader(); 
         int idx=1,index=-1;
        //Reader header and do nothing
        try {
             line = buffer.readLine();
             String[] rec = line.split(",");
             
             for(int i=0;i<rec.length;i++){
               
                 if(rec[i].equals(level)){
                    index=i;
                    break;
                 }
                
                /* if(rec[i].equals("Genus")){
                    return idx;
                 }
                 if(rec[i].equals("Species")){
                    return idx;
                 }
                 if(rec[i].equals("Family")){
                    return idx;
                 }
                  if(rec[i].equals("Origin")){
                    return idx;
                 }
                   if(rec[i].equals("Form")){
                    return idx;
                 }
                   if(rec[i].equals("Use")){
                    return idx;
                 }
                    if(rec[i].equals("Subject")){
                    return idx;
                 }
                    if(rec[i].equals("Group")){
                    return idx;
                 }
                   if(rec[i].equals("Photo-Type")){
                    return idx;
                 }
                   if(rec[i].equals("Common Name2")){
                    return idx;
                 }
                    if(rec[i].equals("Common Name3")){
                    return idx;
                 }
                     if(rec[i].equals("Common Name4")){
                    return idx;
                 }*/
                     
             }
             
             
        } catch (IOException ec) {
            
        }
         return index;
     }

    // private String[] treeToList(SortedSet taxaList) {//Commnted by preethy
    public String[] treeToList(SortedSet taxaList) {
        String[] theList = new String[taxaList.size()];
        Iterator iter = taxaList.iterator();
        String temp;

        int i = 0;
        while (true) {
            try {
                temp = (String) iter.next();
                theList[i] = temp;
                i++;
            } catch (NoSuchElementException ec) {
                break;
            }
        }
        return theList;
    }

    private void TrimRecords(int[] imgsPerTaxa, ArrayList imageFileNames, int max) {
        // Trim records off the top - whichever taxas have the most images.

        //TODO: fix this part
        if (taxonomy.size() <= max) {
            return;
        /////////////////////////
        }
        int total = 0;
        int largest = 0;
        int num = imgsPerTaxa.length;
        //Get total num of images, and find the one with the most.
        for (int i = 0; i < num; i++) {
            total += imgsPerTaxa[i];
            if (imgsPerTaxa[i] > largest) {
                largest = imgsPerTaxa[i];
            }
        }


        if (total <= max) {
            return;
        }
        int i = 0;
        int r;
        int ptr = 0;
        Random generator = new Random();
        while (total > max) {
            // If imgsPerTaxa[i] > largest, delete one.
            // If all have been deleted from a taxa, skip.
            if (imgsPerTaxa[i] >= largest && imgsPerTaxa[i] > 0) {
                r = generator.nextInt(imgsPerTaxa[i]) + ptr;  //remove random element.
                imageFileNames.remove(r);
                taxonomy.remove(r);
                total--;
                imgsPerTaxa[i]--;
            }
            ptr += imgsPerTaxa[i];
            i++;
            // Once checked entire collection, begin at beginning,
            // Decrease largest by one, since one was deleted in at least one taxa.
            if (i == num) {
                i = 0;
                largest--;
                ptr = 0;
            }
        }
    }
    
    //private BufferedReader myFileReader()  {//Commented by preethy
     public BufferedReader myFileReader()  {
        FileReader inFile = null;
        //TODO: remove commented-out code.
       // String currentDirectory = Configuration.ApplicationPath();
        String path = this.DatabasePath();
        File checkFile = new File(path);
        if(checkFile.exists() == false)
            return null;
        try {
            inFile = new FileReader(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataBaseDriver.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new BufferedReader(inFile);
    }

     public BufferedReader myFieldRelationshipsReader()  {
        FileReader inFile = null;
        //TODO: remove commented-out code.
       // String currentDirectory = Configuration.ApplicationPath();
        String path = this.FieldRelationshipsPath();
        File checkFile = new File(path);
        if(checkFile.exists() == false)
            return null;
        try {
            inFile = new FileReader(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataBaseDriver.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new BufferedReader(inFile);
    }

    private String DatabasePath(){
        //System.out.println("mDataBaseName--->"+Configuration.UserPath()+"Database/"+mDataBaseName);
       // return Configuration.getDatabaseFolderPath() + "//" + mDataBaseName;
       
       /* return (new StringBuilder()).
                append(Configuration.ApplicationPath()).
                append("/DataBase").
                append("/"+mDataBaseName).toString();*/
       
        
        return Configuration.ApplicationPath()+ "/" + DATABASE_FOLDER + "/" + mDataBaseName;
      //  return Configuration.UserPath()+"/Database/"+mDataBaseName;
    }

    private String FieldRelationshipsPath(){
        
        return Configuration.ApplicationPath() + "/" + DATABASE_FOLDER + "/" + FIELD_RELATIONSHIPS_FILE;
    }

    private void RandomizeArray(int[] arr) {
        int temp;
        int myRandomNumber;

        if (arr.length == 1) {
            return;
        }
        Random generator = new Random();
        for (int i = 0; i < arr.length; i++) {
            myRandomNumber = generator.nextInt(arr.length - 1);
            temp = arr[myRandomNumber];
            arr[myRandomNumber] = arr[i];
            arr[i] = temp;
        }
    }

    private String[][] AddFileExtension(ArrayList imageFileNames, ArrayList levels) {
        String[][] records = new String[imageFileNames.size()][2];
        
        int i = 0;
       
        while (i < records.length) {
          //  String temp = "0000" + (String) imageFileNames.get(i);//Commented by preethy on 18-01-2012
            //Added by preethy on 18-01-2012
           /* String temp = "0" + (String) imageFileNames.get(i);
            if(temp.length()==3)
              temp = temp.substring(temp.length() - 2);
            else if(temp.length()>3)
              temp = temp.substring(temp.length() - 3);  
           */
            /***/
            String temp =  (String) imageFileNames.get(i);
            
          //  temp = temp.substring(temp.length() - 5);//Commented by preethy on 18-01-2012
            records[i][0] = temp + ".jpg";
            records[i][1] = (String) levels.get(i);
         
            i++;
            
        }
               
        return records;
    }
    //Added by preethy on 05-01-2012
    public String[] getValues(String level) {
            int idx = GetIndex2(level);
            BufferedReader buffer = myFileReader();
            SortedSet taxaList = GetList(buffer, level, idx, -1);
            CloseBuffer(buffer);
            return treeToList(taxaList);
    }
   
    public void  setSubject(ArrayList selectedValues) {
        
        if(subject.size()!=0)
         {
             selctedsub=true;
             subject=new ArrayList();
         }
       for(int i=0;i<selectedValues.size();i++)
         {
             subject.add(selectedValues.get(i));
         }
       if(!(selectedFilters.contains("subject")))
         {
             selectedFilters.add("subject");
         }
             
    }
   public void  setGroup(ArrayList selectedValues) {
        if(group.size()!=0)
         {
             selectedgrp=true;
             group=new ArrayList();
         }
       for(int i=0;i<selectedValues.size();i++)
         {
             group.add(selectedValues.get(i));
         }
      if(!(selectedFilters.contains("Group")))
         {
             selectedFilters.add("Group");
         }
   }  
   public void  setOrigin(ArrayList selectedValues) {
       if(origin.size()!=0)
         {
             selectedorg=true;
             origin=new ArrayList();
         }
      for(int i=0;i<selectedValues.size();i++)
         {
             origin.add(selectedValues.get(i));
         }
       if(!(selectedFilters.contains("origin")))
         {
             selectedFilters.add("origin");
         }
   }  
   public void  setForm(ArrayList selectedValues) {
       if(form.size()!=0)
         {
             selectedform=true;
             form=new ArrayList();
         }
       for(int i=0;i<selectedValues.size();i++)
         {
             form.add(selectedValues.get(i));
         }
       if(!(selectedFilters.contains("form")))
         {
             selectedFilters.add("form");
         }
    }  
   public void  setUse(ArrayList selectedValues) {
       if(use.size()!=0)
         {
             selecteduse=true;
             use=new ArrayList();
         }
       for(int i=0;i<selectedValues.size();i++)
         {
             use.add(selectedValues.get(i));
         }
       if(!(selectedFilters.contains("use")))
         {
             selectedFilters.add("use");
         }
    }
   public void  setPhototype(ArrayList selectedValues) {
     
       if(phototype.size()!=0)
         {
             selectedph_type=true;
             phototype=new ArrayList();
         }
       for(int i=0;i<selectedValues.size();i++)
         {
             phototype.add(selectedValues.get(i));
         }
       if(!(selectedFilters.contains("Photo-Type")))
         {
             selectedFilters.add("Photo-Type");
         }
       
    }


    /*
     * GetMatchingGroupItems
     * 
     * Function to find all valid group items that match the items already checked. This
     * allows the program to dynamically hide and show checkboxes based on what could
     * possibly be chosen. For example, if an origin of "exotic" is chosen, and no images
     * exist with an origin of "exotic" and a form of "tree", then "tree" will not be
     * returned as a valid choice and can thus be hidden.
     * 
     * This function also returns the count of rows matched, which is the image count,
     * and it adds the matching database rows into the DataBaseDriver selectedRow
     * variable (which is initialized and set on every pass through this function).
     * 
     * The ArrayList returned has four elements:
     * 
     * result[0] is the count of matching rows
     * result[1] is the ArrayList of matching group items
     * result[2] is the index number of the group just clicked
     * result[3] is an ArrayList of any checked items that are being removed
     * 
     */

    public ArrayList GetMatchingGroupItems(ArrayList selectedNames, String sourceGroup, ArrayList header) throws IOException {
        BufferedReader buffer = myFileReader(); //Returns file reader

        String row = "";
        String[] rec;
        int index, groupIndex;
        selectedRow = new ArrayList();

        // Read the header row. Header is already passed into this function, but
        // we need to know the start and end of the header columns. Ultimately,
        // we can test for this in the getHeader function itself since all
        // databases should have the same basic structure, and then the next several
        // lines will become unnecessary.
        try {
            row = buffer.readLine();
        } catch (IOException ec) {
            //TODO:  Something.
        }

        // split the comma-separated line into an arraylist
        rec = row.split("\\s*,\\s*");
        int headerStart = -1;
        int headerEnd = -1;
        int sourceIndex = -1;
        String termDb, termChecked;

        for (index = 0; index < rec.length; index++) {
            termDb = rec[index].toLowerCase();
            rec[index] = termDb;
            if (termDb.equalsIgnoreCase("family"))
                headerStart = index + 1;
            if (termDb.equalsIgnoreCase("common name 2"))
                headerEnd = index - 1;
            if (termDb.equalsIgnoreCase(sourceGroup))
                sourceIndex = index;
        }

        int total = 0;
        boolean foundMatch, allItemsMatch, allOtherItemsMatch;
        ArrayList terms;
        String[] termDbArray;
        ArrayList resultSelected = new ArrayList();
        ArrayList toCheck = new ArrayList();
        ArrayList foundFlag = new ArrayList();
        
        // Every time we read a row, we'll look at each value and see if we need to
        // add it to our results. If there are 6 columns, then we have to check each
        // column for a match and add all the other columns to results if it matches.
        // This may create a bunch of redundant adds if several columns are checked.
        // Therefore, we'll keep track of what items are added each pass so that we
        // don't waste time re-evaluating and re-adding more than once. We create
        // termsToAddMaster so that it's easy to reset termsToAdd for each pass.
        ArrayList termsToAddMaster = new ArrayList();
        ArrayList termsToAdd;
        for (index = 0; index < selectedNames.size(); index++) {
            termsToAddMaster.add(index);
        }

        // All items in all groups which should be displayed will be added to
        // resultGroups, which needs to be an ArrayList of as many ArrayLists as
        // there are groups.
        ArrayList resultGroups = new ArrayList();
        for (Object singleGroup : selectedNames) {
            //resultGroups.add(new ArrayList((ArrayList)singleGroup));
            resultGroups.add(new ArrayList());
        }

        // Item 0 in result is an int (the count of matching items); item 1 is
        // an ArrayList of groups.
        ArrayList result = new ArrayList();
        result.add(0, 0);
        result.add(1, resultGroups);

        // We only need to scan those columns that have checked items, so we create
        // the toCheck array list to tell the program which columns need to be scanned
        // for checked group items. In addition, we track whether at least one group
        // is checked (if not, all values get returned so that the groups are reset).
        // Finally, we check to see if exactly one group is checked; if so, all its
        // options are returned since we never want to restrict a group's options if
        // it is the only group checked.

        boolean isAnythingChecked = false;
        int singleCheckedIndex = -1; // if only one group is checked, this will hold its index
        
        ArrayList groupJList, groupList;
        for (Object groupObj1 : selectedNames) {
            groupList = (ArrayList) groupObj1;
            if (!groupList.isEmpty()) {
                if (!isAnythingChecked) {
                    isAnythingChecked = true;
                    singleCheckedIndex = selectedNames.indexOf(groupList);
                }
                else
                    singleCheckedIndex = -1;
                
                toCheck.add(selectedNames.indexOf(groupList));
                foundFlag.add(false);
            }
        }

        // If nothing is checked, then set foundFlag true for each column. That way,
        // the system will iterate through and add all possible values, in essence
        // resetting all columns to have all values.
        if (!isAnythingChecked) {
            for (index = 0; index < selectedNames.size(); index++) {
                foundFlag.add(true);
            }
        }

        // Iterate over the remainder of the database. buffer.ready() returns false
        // when there's no more data to be read, so it's an elegant iteration control.
        while (buffer.ready()) {
            try {
                row = buffer.readLine();
            }
            catch (IOException ec) {
                //TODO: Some code
            }
            allItemsMatch = true;
            allOtherItemsMatch = true;
            rec = row.split("\\s*,\\s*");
            
            // Only look for matches if something is checked; otherwise, all values
            // need to be returned. Counting occurs in either case.
            if (isAnythingChecked) {
                for (index = 0; index < toCheck.size(); index++) {
                    groupIndex = ((Integer) toCheck.get(index)).intValue();
                    Object groupObj2 = selectedNames.get(groupIndex);
                    ArrayList groupArray = (ArrayList) groupObj2;

                    termDb = rec[groupIndex + headerStart];
                    foundMatch = false;

                    if (!termDb.isEmpty()) {
                        findMatch:
                        for (Object groupItem : groupArray) {
                            termChecked = groupItem.toString();
                            termDbArray = parseMultiSelect(termDb);
                            for (String termDbValue : termDbArray) {
                                if (termChecked.equals(termDbValue)) {
                                    foundMatch = true;
                                    break findMatch;
                                }
                            }
                        }
                    }
                    foundFlag.set(index, foundMatch); // this way, we know each DB col that did or didn't match
                    if (!foundMatch) {
                        allItemsMatch = false; // if even one mismatches, the whole row mismatches for counting purposes
                    }
                }
            }

           // If all items were matched, add 1 to the count and add the row to
            // selectedRow (a class variable that holds all rows with selected
            // images).
            if (allItemsMatch) {
                total++;  // Up the count by 1 for each matching row
                if (isAnythingChecked)
                    selectedRow.add(rec);
            }

            // Figure out whether each column's term should be added to the results
            // for being shown as a checkbox. If the term in question is _not_ in
            // a group with at least one checked item, it should be returned only if
            // all items match. However, if the term in question _is_ in a group
            // with at least one checked item, it should be returned only if all
            // _other_ items (besides itself) are checked. This keeps us from
            // restricting a group to only its own checked items.
            boolean addTerm;
            for (int j = 0; j < resultGroups.size(); j++) {
                // If all items match, we know we can add the term no matter what.
                // Therefore, start off addTerm with the value of allItemsMatch.
                addTerm = allItemsMatch;

                // Now, if allItemsMatch is false, then addTerm is false. Next,
                // we may still be able to add the term if j (the index of the
                // term we're considering) is in the toCheck array. In this case,
                // if all _other_ terms match, we can add the term.
                if (!addTerm && toCheck.contains((Object) j)) {
                    addTerm = true;
                    // Now iterate through all foundFlags, skipping the one that
                    // corresponds to the current column j. If any are false, don't
                    // add the term. Otherwise, add the term. This gives us the
                    // chance to add a term that didn't match, but where all other
                    // columns did match. (NOTE: If only one column is checked,
                    // then it is automatically added since no other columns are
                    // checked, and thus the loop only runs once and exits.)
                    //for (Object columnMatch : foundFlag) {
                    for (int k = 0; k < foundFlag.size(); k++) {
                        if (((Integer) toCheck.get(k)).intValue() != j && foundFlag.get(k).equals(false)) {
                            addTerm = false;
                            break;
                        }
                    }
                }
                // After all that, if addTerm is true, check to make sure the term
                // hasn't been added already, and then add it if it hasn't been.
                if (addTerm) {
                    terms = (ArrayList) resultGroups.get(j);
                    termDb = rec[j + headerStart];
                    termDbArray = parseMultiSelect(termDb);
                    for (String strArray1: termDbArray) {
                        if (terms.indexOf(strArray1) < 0 && !strArray1.isEmpty())
                            terms.add(strArray1);
                    }
                }
            }
 
        }

        if (isAnythingChecked) {
            // Assuming at least one column is checked, see if there are any
            // FieldRelationships defined. If there are, find all possible values
            // for each checked field's dependent fields, then make sure the
            // corresponding result groups don't show any values that aren't
            // present in the relationships file for the field in question.
            if (fieldRelationshipReference != null) {
                ArrayList allRelationshipItems = new ArrayList();
                for (index = 0; index < resultGroups.size(); index++)
                    allRelationshipItems.add(new ArrayList());
                ArrayList selectedNameSet, relationshipItems, reference, setList, setListMaster, setListDep;
                int selectedIndex, masterIndex, masterCol, depCol, setIndex, setDir, setValIndex;
                String setMasterVal, setDepVal;
                for (selectedIndex = 0; selectedIndex < selectedNames.size(); selectedIndex++) {
                    selectedNameSet = (ArrayList) selectedNames.get(selectedIndex);
                    if (!selectedNameSet.isEmpty()) {
                        for (masterIndex = 0; masterIndex < fieldRelationshipMasterFields.size(); masterIndex++) {
                            masterCol = ((Integer) fieldRelationshipMasterFields.get(masterIndex)).intValue();
                            if (masterCol == selectedIndex) {
                                depCol = ((Integer) fieldRelationshipDepFields.get(masterIndex)).intValue();
                                relationshipItems = (ArrayList) allRelationshipItems.get(depCol);
                                reference = (ArrayList) fieldRelationshipReference.get(masterIndex);
                                setIndex = ((Integer) reference.get(0)).intValue();
                                setDir = ((Integer) reference.get(1)).intValue();
                                setList = (ArrayList) fieldRelationshipSets.get(setIndex);
                                setListMaster = (ArrayList) setList.get(5 + setDir); // 5 if setDir is 0 (normal rel), 6 if setDir is 1 (reciprocal rel)
                                setListDep = (ArrayList) setList.get(6 - setDir); // 6 if setDir is 0 (normal rel), 5 if setDir is 1 (reciprocal rel)
                                for (setValIndex = 0; setValIndex < setListMaster.size(); setValIndex++) {
                                    setMasterVal = (String) setListMaster.get(setValIndex);
                                    setDepVal = (String) setListDep.get(setValIndex);
                                    if (selectedNameSet.contains(setMasterVal) && !relationshipItems.contains(setDepVal)) {
                                        relationshipItems.add(setDepVal);
                                    }
                                }
                            }
                        }
                    }
                }
                for (index = 0; index < allRelationshipItems.size(); index++) {
                    ArrayList relItems = (ArrayList) allRelationshipItems.get(index);
                    if (!relItems.isEmpty()) {
                        ((ArrayList) resultGroups.get(index)).retainAll(relItems);
                    }
                }
            }
            result.set(0, total);  // otherwise, result[0] already equals 0
        }

        // Compare the values to be shown with the values that were checked to begin
        // with. If any of the checked values are going away, we need to uncheck them,
        // highlight them, and warn the user.
        ArrayList groupDiff = new ArrayList();
        for (index = 0; index < selectedNames.size(); index++) {
            // groupDiff will start as a clone of selectedNames (thus containing all
            // currently checked items). Then, one group at a time, we remove any
            // checked items that are being returned as groups to show. Anything
            // left over is a checked item that's going away. When done, we'll
            // return groupDiff as a part of the return ArrayList so the display
            // engine can warn the user.
            groupDiff.add(((ArrayList) selectedNames.get(index)).clone());
            ((ArrayList) groupDiff.get(index)).removeAll((ArrayList) resultGroups.get(index));
        }

        // Return the index of the clicked group if more than one group was selected.
        // This way, the calling function knows which column to skip when removing
        // group values.
        if (isAnythingChecked && singleCheckedIndex == -1)
            result.add(2, sourceIndex - headerStart);
        else
            result.add(2, -1);

        // Return groupDiff as the final element in the return ArrayList.
        result.add(3, groupDiff);

        return result;
    }

    /*
     * This function accepts a string and checks it to see if it starts and ends with
     * a predetermined delimiter (currently a semicolon, though we could overload it
     * with a function that accepts a delimiter as a second option). If so, it splits
     * the string into an array based on the semicolon. If not, it returns the original
     * string wrapped in an array so that the calling function can iterate on an array
     * no matter what.
     *
     * @param source
     * @return 
     */
    private String[] parseMultiSelect(String source) {
        String[] result;
        // Since a database field can contain multiple values delimited with
        // semicolons (including start and end semicolons), test for this and
        // break into discreet values if appropriate
        if (source.startsWith(";") && source.endsWith(";")) {
            source = source.substring(1, source.length() - 1);
            result = source.split(";");
        }
        else if (source.endsWith(";")) {
            source = source.substring(0, source.length() - 1);
            result = source.split(";");
        }
        else {
            result = new String[1];
            result[0] = source;
        }
        return result;
    }

    /*
    Function to read a FieldRelationships.csv file (if one exists), validate it,
    and use it to define formal relationships between fields to control how they
    show/hide group values based on each other (overriding the normal use of the
    database to determine which fields show/hide).
    */
    public String GetFieldRelationships() {

        ArrayList header = GetHeader();

        BufferedReader buffer = myFieldRelationshipsReader(); //Returns relationships file

        String row = "";
        String rowVal;
        String[] rec;
        ArrayList resultCounts = new ArrayList();
        ArrayList resultValues = new ArrayList();
        int j, k, m;
        String returnState = "success";

        try {
            if (buffer != null)
                row = buffer.readLine();
            else {
                fieldRelationshipSets = null;
                return "no file";
            }
        } catch (IOException ec) {
            fieldRelationshipSets = null;
            return "read error";
        }

        rec = row.split("\\s*,\\s*");
        
        int count = 0;
        String master = "";
        String dependentValue = "";
        ArrayList dependent = new ArrayList();
        boolean setError = false;
        ArrayList currentValue = new ArrayList();
        for (j = 0; j < rec.length; j++) {
            rowVal = rec[j];
            if (rowVal.equalsIgnoreCase("reciprocal") || j == rec.length - 1) {
                resultCounts.add(count); // number of columns in set, not counting Reciprocal
                if (setError)
                    resultValues.add(new ArrayList());
                else {
                    for (k = 0; k < dependent.size(); k++) {
                        dependentValue = (String) dependent.get(k);
                        currentValue.add(0, header.indexOf(master));
                        currentValue.add(1, header.indexOf(dependentValue));
                        currentValue.add(2, master);
                        currentValue.add(3, dependentValue); // the dependent column name
                        currentValue.add(4, true); // t/f for Reciprocal (may change later)
                        currentValue.add(5, new ArrayList()); // master values
                        currentValue.add(6, new ArrayList()); // dependent values
                        resultValues.add(currentValue.clone());
                        currentValue = new ArrayList();
                    }
                }
                setError = false;
                dependent = new ArrayList();
                count = 0;
                master = "";
            }
            // Process the next value as long as we haven't already encountered an
            // error in this set.
            else {
                if (!setError) {
                    // If the current value is not a valid fieldname, then
                    // mark this set to be skipped by setting setError to true.
                    if (!header.contains(rowVal)) {
                        setError = true;
                        returnState = "invalid header value";
                    }
                    else if (master.equals(rowVal) || dependent.contains(rowVal)) {
                        setError = true;
                        returnState = "duplicate in set";
                    }
                    else {
                        if (count == 0)
                            master = rowVal;
                        else {
                            dependent.add(rowVal);
                        }
                    }
                }
                count++;
            }
        }

        // Now that we have the headers and counts stored, let's store the values.
        
        boolean firstRow = true;
        boolean reciprocal;
        int columnIndex = 0;
        int resultIndex = 0;
        int tempResultIndex, tempColumnIndex, resultCount;
        String[] masterArray, dependentArray;
        ArrayList tempValues;
        try {
            while (buffer.ready()) { // this keeps looping as long as the file has unread rows
                row = buffer.readLine();
                rec = row.split("\\s*,\\s*");
                for (Object resultCountObj : resultCounts) {
                    resultCount = ((Integer) resultCountObj).intValue();
                    if (
                        ((ArrayList) resultValues.get(resultIndex)).isEmpty() ||
                        columnIndex >= rec.length ||
                        rec[columnIndex].equals("")
                    ) {
                        resultIndex += 1;
                        columnIndex += resultCount + 1;
                    }
                    else {
                        if (rec.length <= columnIndex + resultCount)
                            reciprocal = true;
                        else
                            reciprocal = !rec[columnIndex + resultCount].equalsIgnoreCase("no");
                        if (rec.length <= columnIndex)
                            masterArray = new String[0];
                        else
                            masterArray = parseMultiSelect(rec[columnIndex]);
                        for (j = 0; j < masterArray.length; j++) {
                            tempColumnIndex = columnIndex;
                            tempResultIndex = resultIndex;
                            for (k = 1; k < resultCount; k++) {
                                tempValues = (ArrayList) resultValues.get(tempResultIndex);
                                tempValues.set(4, reciprocal);
                                tempColumnIndex++;
                                if (rec.length <= columnIndex)
                                    dependentArray = new String[0];
                                else
                                    dependentArray = parseMultiSelect(rec[tempColumnIndex]);
                                for (m = 0; m < dependentArray.length; m++) {
                                    ((ArrayList) tempValues.get(5)).add(masterArray[j]);
                                    ((ArrayList) tempValues.get(6)).add(dependentArray[m]);
                                }
                                tempResultIndex++;
                            }
                        }
                        resultIndex += 1;
                        columnIndex += resultCount + 1;
                    }
                }
                firstRow = false;
                resultIndex = 0;
                columnIndex = 0;
            }
        } catch (IOException ex) {
            Logger.getLogger(DataBaseDriver.class.getName()).log(Level.SEVERE, null, ex);
            return "read error";
        }

        // Read the resultValues if there are any. Figure out which header
        // elements are affected and put them in a ListArray. This lets us more
        // efficiently use the data when it comes time to run GetMatchingGroupItems.
        ArrayList masterFields = new ArrayList();
        ArrayList depFields = new ArrayList();
        ArrayList relRefs = new ArrayList();
        int masterIndex, dependentIndex;
        String masterTerm, dependentTerm;
        ArrayList alTemp, alTemp2;
        for (j = 0; j < resultValues.size(); j++) {
            ArrayList relSet = (ArrayList) resultValues.get(j);
            if (!relSet.isEmpty()) {
                masterIndex = ((Integer) relSet.get(0)).intValue();
                dependentIndex = ((Integer) relSet.get(1)).intValue();
                masterFields.add(masterIndex);
                depFields.add(dependentIndex);
                alTemp = new ArrayList();
                alTemp.add(0, j);
                alTemp.add(1, 0);
                relRefs.add(alTemp.clone()); // this stores the index (for master and matching dep); the second term, 0, means a standard master/dep relationship
                if (relSet.get(4).equals(true)) {
                    masterFields.add(dependentIndex);
                    depFields.add(masterIndex);
                    alTemp = new ArrayList();
                    alTemp.add(0, j);
                    alTemp.add(1, 1);
                    relRefs.add(alTemp.clone()); // this stores the index (for master and matching dep); the second term, 1, means a reciprocal relationship
                }
            }
        }

        fieldRelationshipSets = resultValues;
        fieldRelationshipMasterFields = masterFields;
        fieldRelationshipDepFields = depFields;
        fieldRelationshipReference = relRefs;
        return returnState;
    }

    // The following function is deprecated in favor of GetMatchingGroupItems() above.
/*
    public int GetImageCount(String name,ArrayList header) {
        int count=0;
        BufferedReader buffer = myFileReader(); //Returns file reader
        String line;
        String s1,s2,s3,s4,s5,s6;
        int idx1=0,idx2=0,idx3=0,idx4=0,idx5=0,idx6=0;
        Boolean flag=false,flag2=false,flag3=false,flag4=true;
        Boolean gp=false,sb=false,og=false,fm=false,us=false,ph=false;
        ArrayList selectedRowcopy = new ArrayList();
        ArrayList repeatSelection = new ArrayList();
        //Reader header and do nothing
        try {
            line = buffer.readLine();
        } catch (IOException ec) {
            //TODO:  Something.
        }
      
        
        for(int i=0;i<selectedFilters.size();i++)
           {
               
              if(selectedFilters.get(i).equals("subject"))
              {
                 // idx1 = GetIndex2((String)selectedFilters.get(i));//Commented by preethy on 21-06-2012
                  idx1 = GetIndex2((String)header.get(0));//index of subject in csv file
                  sub=true;
                               
              }
               if(selectedFilters.get(i).equals("Group"))
              {
                 // idx2 = GetIndex2((String)selectedFilters.get(i));
                   idx2 = GetIndex2((String)header.get(1));
                  grp=true;
                  
              }
                if(selectedFilters.get(i).equals("origin"))
              {
                 // idx3 = GetIndex2((String)selectedFilters.get(i));
                   idx3 = GetIndex2((String)header.get(2));
                  org=true;
              }
                 if(selectedFilters.get(i).equals("form"))
              {
                 // idx4 = GetIndex2((String)selectedFilters.get(i));
                   idx4 = GetIndex2((String)header.get(3));
                  frm=true;
              }
                  if(selectedFilters.get(i).equals("use"))
              {
                 // idx5 = GetIndex2((String)selectedFilters.get(i));
                   idx5 = GetIndex2((String)header.get(4));
                  use1=true;
                   
              }
               if(selectedFilters.get(i).equals("Photo-Type"))
              {
                 
                //  idx6 = GetIndex2((String)selectedFilters.get(i));
                   idx6 = GetIndex2((String)header.get(5));
                  ph_type=true;
                 
              }  
                                        
           }
            
           
            if(sub && subject.size()==0)
            {
                sub=false;//setting flag valus to false,if there size is zero
            }
            if(grp && (group.size()==0))
            {
               grp=false;
            }
            if(org && (origin.size()==0))
            {
               org=false;
            }
            if(frm && (form.size()==0))
            {
               frm=false;
            }
            if(use1 && (use.size()==0))
            {
               use1=false;
            }
             if(ph_type && (phototype.size()==0))
            {
               ph_type=false;
            }
      if(selctedsub && !grp && !org && !frm && !use1 && !ph_type)
          {
             selectedRow = new ArrayList();
          }
          if(selectedgrp && !sub && !org && !frm && !use1 && !ph_type)
          {
             selectedRow = new ArrayList();
          }
           if(selectedorg && !grp && !sub && !frm && !use1 && !ph_type)
          {
             selectedRow = new ArrayList();
          }
           if(selectedform && !grp && !org && !sub && !use1 && !ph_type)
          {
             selectedRow = new ArrayList();
          }
           if(selecteduse && !grp && !org && !frm && !sub && !ph_type)
          {
             selectedRow = new ArrayList();
          }
            if(selectedph_type && !grp && !org && !frm && !sub && !use1)
          {
             selectedRow = new ArrayList();
          }
           
           while (true) {
               try {
                line = buffer.readLine();
                if (line == null) {
                    break;
                } else {
                    line = line.trim();
                }
               String[] rec = line.split(",");//splitting the lines of csv file
                if(rec.length>4)
                  {
                  if(name.equals("subject"))
                     {
                              
                       for (int j = 0; j < subject.size(); j++) 
                          {
                             if(sub && !grp && !org && !frm && !use1 && !ph_type && !selctedsub) 
                                {
                                   
                                 //  s1= GetTaxaString(rec, idx1); 
                                    s1=rec[idx1];
                                    
                                   if(s1.compareTo((String)subject.get(j)) == 0){//compairing values under subject column to selected check box value
                                   selectedRow.add(rec);//if they are equal adding that row to array list
                                   
                                   }
                                } else if(sub && !grp && !org && !frm && !use1 && !ph_type && (selctedsub)) 
                                {
                                                                       
                                 //  s1= GetTaxaString(rec, idx1);
                                     s1=rec[idx1];
                                   if(s1.compareTo((String)subject.get(j)) == 0){
                                   selectedRow.add(rec);
                                   
                                   }
                                }else if(sub && selctedsub && (grp || org || frm || use1 || ph_type))
                                   {
                                                                            
                                       flag2=true;
                                      // s1= GetTaxaString(rec, idx1);
                                        s1=rec[idx1];
                                       if(s1.compareTo((String)subject.get(j)) == 0){
                                       repeatSelection.add(rec);  }
                                       if(grp) gp=true;//checking whether others are selected or not
                                       if(org) og=true;
                                       if(frm) fm=true;
                                       if(use1) us=true;
                                       if(ph_type) ph=true; 
                                  }
                               else 
                                  {
                                     
                                       flag=true;
                                       for(int i=0;i<selectedRow.size();i++)
                                           {         
                                                String[] a=(String[])selectedRow.get(i);
                                               // s1=GetTaxaString(a, idx1);
                                                 s1=a[idx1];
                                                if(s1.compareTo((String)subject.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(a);  
                                                }
                                           }  
                                                selectedRow = new ArrayList();
                                                if(selectedRowcopy.size()>0)
                                                selectedRow=selectedRowcopy;
                                                selectedRowcopy = new ArrayList();
                                                break;
                                 }
                          }
                      
                       if((subject.size()==0)&&(grp || org || frm || use1 || ph_type))
                                {
                                    
                                    if(grp) gp=true;
                                    if(org) og=true;
                                    if(frm) fm=true;
                                    if(use1) us=true;
                                    if(ph_type) ph=true;
                                    flag3=true;
                                    if(sub)
                                    {
                                       
                                        sb=false;
                                        for (int j = 0; j < subject.size(); j++) 
                                          {
                                            //s1= GetTaxaString(rec, idx1); 
                                               s1=rec[idx1];
                                            if(s1.compareTo((String)subject.get(j)) == 0)
                                            {
                                              selectedRowcopy.add(rec);  
                                            }
                                          }
                                        
                                    }
                                    else if(grp)
                                    {
                                          
                                         gp=false;
                                        for (int j = 0; j < group.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx2); 
                                                 s1=rec[idx2];
                                                if(s1.compareTo((String)group.get(j)) == 0)
                                                {
                                                selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                    else if(org)
                                    {
                                        
                                        og=false;
                                        for (int j = 0; j < origin.size(); j++) 
                                            {
                                              //  s1= GetTaxaString(rec, idx3); 
                                                 s1=rec[idx3];
                                                if(s1.compareTo((String)origin.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(frm)
                                    {
                                         
                                        fm=false;
                                        for (int j = 0; j < form.size(); j++) 
                                            {
                                                //s1= GetTaxaString(rec, idx4); 
                                                 s1=rec[idx4];
                                                if(s1.compareTo((String)form.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(use1)
                                    {
                                        us=false;
                                        for (int j = 0; j < use.size(); j++) 
                                            {
                                              
                                                //s1= GetTaxaString(rec, idx5); 
                                                  s1=rec[idx5];
                                                if(s1.compareTo((String)use.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                       
                                    }
                                     else if(ph_type)
                                    {
                                        ph=false;
                                        for (int j = 0; j < phototype.size(); j++) 
                                            {
                                              
                                               // s1= GetTaxaString(rec, idx6); 
                                                 s1=rec[idx6];
                                                if(s1.compareTo((String)phototype.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                       
                                    }
                                }
                      }
               if(name.equals("group"))
                      {
                       
                        for (int j = 0; j < group.size(); j++) 
                            {
                                if(grp && !sub && !org && !frm && !use1 &&!ph_type && !selectedgrp) 
                                    {
                                       // s1= GetTaxaString(rec, idx2);
                                       
                                         s1=rec[idx2];
                                        if(s1.compareTo((String)group.get(j)) == 0)
                                         {
                                          selectedRow.add(rec);  
                                         }
                                    } 
                                         
                                else if(grp && !sub && !org && !frm && !use1 &&!ph_type && (selectedgrp)) 
                                    {
                                        // s1= GetTaxaString(rec, idx2);
                                          s1=rec[idx2];
                                         if (s1.compareTo((String)group.get(j)) == 0){
                                         selectedRow.add(rec);  
                                         }
                                    }
                                else if(grp && selectedgrp && (sub || org || frm || use1 || ph_type))
                                    {
                                         flag2=true;                        
                                        // s1= GetTaxaString(rec, idx2);
                                          s1=rec[idx2];
                                         if(s1.compareTo((String)group.get(j)) == 0){
                                         repeatSelection.add(rec);  }
                                         if(sub) sb=true;
                                         if(org) og=true;
                                         if(frm) fm=true;
                                         if(use1) us=true;
                                         if(ph_type) ph=true;
                                   }
                               
                               else 
                                   {
                                         flag=true;
                                         for(int i=0;i<selectedRow.size();i++)
                                            {   
                                                String[] a=(String[])selectedRow.get(i);
                                               // s2=GetTaxaString(a, idx2);
                                                 s2=a[idx2];
                                                if(s2.compareTo((String)group.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(a);                                  
                                                }
                                           }  
                                         selectedRow = new ArrayList();
                                         if(selectedRowcopy.size()>0)
                                         selectedRow=selectedRowcopy;
                                         selectedRowcopy = new ArrayList();
                                         break;
                                  }
                            }
                           if((group.size()==0)&&(sub || org || frm || use1 || ph_type))
                                {
                                    if(sub) sb=true;
                                    if(org) og=true;
                                    if(frm) fm=true;
                                    if(use1) us=true;
                                    if(ph_type) ph=true;
                                    flag3=true;
                                    if(sub)
                                    {
                                        sb=false;
                                        for (int j = 0; j < subject.size(); j++) 
                                          {
                                           // s1= GetTaxaString(rec, idx1); 
                                               s1=rec[idx1];
                                            if(s1.compareTo((String)subject.get(j)) == 0)
                                            {
                                              selectedRowcopy.add(rec);  
                                            }
                                          }
                                                                           }
                                    else if(grp)
                                    {
                                         gp=false;
                                        for (int j = 0; j < group.size(); j++) 
                                            {
                                               //s1= GetTaxaString(rec, idx2); 
                                                 s1=rec[idx2];
                                                if(s1.compareTo((String)group.get(j)) == 0)
                                                {
                                                selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                    else if(org)
                                    {
                                        og=false;
                                        for (int j = 0; j < origin.size(); j++) 
                                            {
                                                //s1= GetTaxaString(rec, idx3); 
                                                 s1=rec[idx3];
                                                if(s1.compareTo((String)origin.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(frm)
                                    {
                                        fm=false;
                                        for (int j = 0; j < form.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx4); 
                                                 s1=rec[idx4];
                                                if(s1.compareTo((String)form.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(use1)
                                    {
                                        us=false;
                                        for (int j = 0; j < use.size(); j++) 
                                            {
                                                //s1= GetTaxaString(rec, idx5); 
                                                 s1=rec[idx5];
                                                if(s1.compareTo((String)use.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(ph_type)
                                    {
                                        ph=false;
                                        for (int j = 0; j < phototype.size(); j++) 
                                            {
                                                //s1= GetTaxaString(rec, idx6); 
                                                 s1=rec[idx6];
                                                if(s1.compareTo((String)phototype.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                }
                        
                      }
               if(name.equals("origin"))
                      {
                         for (int j = 0; j < origin.size(); j++) 
                            {
                                   if(org && !sub && !grp && !frm && !use1 && !ph_type && !selectedorg) 
                                        {
                                           // s1= GetTaxaString(rec, idx3); 
                                             s1=rec[idx3];
                                            if(s1.compareTo((String)origin.get(j)) == 0)
                                                {
                                                    selectedRow.add(rec);  
                                                }
                                        } 
                                    else if(org && !sub && !grp && !frm && !use1 && !ph_type && (selectedorg)) 
                                        {
                                           // s1= GetTaxaString(rec, idx3);
                                             s1=rec[idx3];
                                            if(s1.compareTo((String)origin.get(j)) == 0){
                                            selectedRow.add(rec);  
                                            }
                                        }          
                                    else if(org && selectedorg && (sub || grp || frm || use1 || ph_type))
                                        {   
                                            flag2=true;   
                                            //s1= GetTaxaString(rec, idx3);
                                             s1=rec[idx3];
                                            if(s1.compareTo((String)origin.get(j)) == 0){
                                            repeatSelection.add(rec);  
                                            }
                                            if(sub) sb=true;
                                            if(grp) gp=true;
                                            if(frm) fm=true;
                                            if(use1) us=true;
                                             if(ph_type) ph=true;
                                         }
                                    else {
                                            flag=true;
                                            for(int i=0;i<selectedRow.size();i++)
                                            {   
                                                String[] a=(String[])selectedRow.get(i);
                                               // s2=GetTaxaString(a, idx3);
                                                 s2=a[idx3];
                                                if(s2.compareTo((String)origin.get(j)) == 0)
                                                    {
                                                        selectedRowcopy.add(a);  
                                                    }
                                            }  
                                                selectedRow = new ArrayList();
                                                if(selectedRowcopy.size()>0)
                                                selectedRow=selectedRowcopy;
                                                selectedRowcopy = new ArrayList();
                                                break;
                                         }
                               }
                                if((origin.size()==0)&&(sub || grp || frm || use1 || ph_type))
                                {
                                    if(sub) sb=true;
                                    if(grp) gp=true;
                                    if(frm) fm=true;
                                    if(use1) us=true;
                                    if(ph_type) ph=true;
                                    flag3=true;
                                    if(sub)
                                    {
                                        sb=false;
                                        for (int j = 0; j < subject.size(); j++) 
                                          {
                                           // s1= GetTaxaString(rec, idx1); 
                                               s1=rec[idx1];
                                            if(s1.compareTo((String)subject.get(j)) == 0)
                                            {
                                              selectedRowcopy.add(rec);  
                                            }
                                          }
                                        
                                    }
                                    else if(grp)
                                    {
                                         gp=false;
                                        for (int j = 0; j < group.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx2); 
                                                 s1=rec[idx2];
                                                if(s1.compareTo((String)group.get(j)) == 0)
                                                {
                                                selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                    else if(org)
                                    {
                                        og=false;
                                        for (int j = 0; j < origin.size(); j++) 
                                            {
                                              //  s1= GetTaxaString(rec, idx3); 
                                                 s1=rec[idx3];
                                                if(s1.compareTo((String)origin.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(frm)
                                    {
                                        fm=false;
                                        for (int j = 0; j < form.size(); j++) 
                                            {
                                                //s1= GetTaxaString(rec, idx4); 
                                                 s1=rec[idx4];
                                                if(s1.compareTo((String)form.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(use1)
                                    {
                                        us=false;
                                        for (int j = 0; j < use.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx5); 
                                                 s1=rec[idx5];
                                                if(s1.compareTo((String)use.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(ph_type)
                                    {
                                        ph=false;
                                        for (int j = 0; j < phototype.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx6); 
                                                  s1=rec[idx6];
                                                if(s1.compareTo((String)phototype.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                }
                               
                 
                        }
                 if(name.equals("form"))
                        {
                            for (int j = 0; j < form.size(); j++) 
                                {
                                    if(frm && !sub && !grp && !org && !use1 && !ph_type && !selectedform) 
                                        {
                                           // s1= GetTaxaString(rec, idx4);
                                             s1=rec[idx4];
                                            if(s1.compareTo((String)form.get(j)) == 0)
                                            {
                                              selectedRow.add(rec);  
                                            }
                                        } 
                                         
                                    else if(frm && !sub && !grp && !org && !use1 && !ph_type && (selectedform)) 
                                        {
                                           // s1= GetTaxaString(rec, idx4);
                                             s1=rec[idx4];
                                            if(s1.compareTo((String)form.get(j)) == 0){
                                            selectedRow.add(rec);  
                                            }
                                        }
                                    else if(frm && selectedform && (sub || grp || org || use1 || ph_type))
                                        {
                                            flag2=true;   
                                           // s1= GetTaxaString(rec, idx4);
                                             s1=rec[idx4];
                                            if(s1.compareTo((String)form.get(j)) == 0){
                                            repeatSelection.add(rec);}
                                            if(sub) sb=true;
                                            if(grp) gp=true;
                                            if(org) og=true;
                                            if(use1) us=true;
                                            if(ph_type) ph=true;
                                        }
                                    else 
                                        {
                                            flag=true;
                                            for(int i=0;i<selectedRow.size();i++)
                                                {  
                                                    String[] a=(String[])selectedRow.get(i);
                                                   // s2=GetTaxaString(a, idx4);
                                                     s2=a[idx4];
                                                    if(s2.compareTo((String)form.get(j)) == 0)
                                                     {
                                                    selectedRowcopy.add(a);  
                                                     }
                                                }  
                                                    selectedRow = new ArrayList();
                                                    if(selectedRowcopy.size()>0)
                                                    selectedRow=selectedRowcopy;
                                                    selectedRowcopy = new ArrayList();
                                                    break;
                                        }
                      
                               }
                                if((form.size()==0)&&(sub || grp || org || use1 || ph_type))
                                {
                                    if(sub) sb=true;
                                    if(grp) gp=true;
                                    if(org) og=true;
                                    if(use1) us=true;
                                    if(ph_type) ph=true;
                                    flag3=true;
                                    if(sub)
                                    {
                                        sb=false;
                                        for (int j = 0; j < subject.size(); j++) 
                                          {
                                           // s1= GetTaxaString(rec, idx1); 
                                               s1=rec[idx1];
                                            if(s1.compareTo((String)subject.get(j)) == 0)
                                            {
                                              selectedRowcopy.add(rec);  
                                            }
                                          }
                                       }
                                    else if(grp)
                                    {
                                         gp=false;
                                        for (int j = 0; j < group.size(); j++) 
                                            {
                                                //s1= GetTaxaString(rec, idx2); 
                                                 s1=rec[idx2];
                                                if(s1.compareTo((String)group.get(j)) == 0)
                                                {
                                                selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                    else if(org)
                                    {
                                        og=false;
                                        for (int j = 0; j < origin.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx3);
                                                
                                                 s1=rec[idx3];
                                                if(s1.compareTo((String)origin.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(frm)
                                    {
                                        fm=false;
                                        for (int j = 0; j < form.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx4);
                                                 s1=rec[idx4];
                                                if(s1.compareTo((String)form.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(use1)
                                    {
                                        us=false;
                                        for (int j = 0; j < use.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx5);
                                                 s1=rec[idx5];
                                                if(s1.compareTo((String)use.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(ph_type)
                                    {
                                        ph=false;
                                        for (int j = 0; j < phototype.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx6); 
                                                 s1=rec[idx6];
                                                if(s1.compareTo((String)phototype.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                    
                                }
                        }
                 if(name.equals("use"))
                        {
                            for (int j = 0; j < use.size(); j++) 
                                {
                                    if(use1 && !sub && !grp && !org && !frm && !ph_type && !selecteduse) 
                                        {
                                            // s1= GetTaxaString(rec, idx5); 
                                               s1=rec[idx5];
                                             if(s1.compareTo((String)use.get(j)) == 0)
                                                {
                                                selectedRow.add(rec);  
                                                }
                                         } 
                                    else if(use1 && !sub && !grp && !org && !frm && !ph_type  && (selecteduse)) 
                                         {
                                             //s1= GetTaxaString(rec, idx5);
                                               s1=rec[idx5];
                                             if (s1.compareTo((String)use.get(j)) == 0){
                                             selectedRow.add(rec);  
                                              }
                                         }
                                    else if(use1 && selecteduse && (sub || grp || org || frm || ph_type))
                                         {
                                             flag2=true;
                                            //s1= GetTaxaString(rec, idx5);
                                             s1=rec[idx5];
                                             if(s1.compareTo((String)use.get(j)) == 0){
                                             repeatSelection.add(rec);  
                                             }
                                            if(sub) sb=true;
                                            if(grp) gp=true;
                                            if(org) og=true;
                                            if(frm) fm=true;
                                            if(ph_type) ph=true;
                                        }
                                    else 
                                        {
                                            flag=true;
                                            for(int i=0;i<selectedRow.size();i++)
                                                {    
                                                    String[] a=(String[])selectedRow.get(i);
                                                    //s2=GetTaxaString(a, idx5);
                                                    s2=a[idx5];
                                                    if(s2.compareTo((String)use.get(j)) == 0)
                                                    {
                                                     selectedRowcopy.add(a);
                                                    }
                                                }  
                                                selectedRow = new ArrayList();
                                                if(selectedRowcopy.size()>0)
                                                selectedRow=selectedRowcopy;
                                                selectedRowcopy = new ArrayList();
                                                break;
                                         }
                      
                                   } 
                                    if((use.size()==0)&&(sub || grp || frm || org || ph_type))
                                    {
                                    if(sub) sb=true;
                                    if(grp) gp=true;
                                    if(frm) fm=true;
                                    if(org) og=true;
                                    if(ph_type) ph=true;
                                    flag3=true; 
                                    if(sub)
                                    {
                                        sb=false; 
                                        for (int j = 0; j < subject.size(); j++) 
                                          {
                                           // s1= GetTaxaString(rec, idx1); 
                                              s1=rec[idx1];
                                            if(s1.compareTo((String)subject.get(j)) == 0)
                                            {
                                              selectedRowcopy.add(rec);  
                                            }
                                          }
                                       
                                    }
                                    else if(grp)
                                    {
                                         gp=false;
                                        for (int j = 0; j < group.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx2); 
                                                s1=rec[idx2];
                                                if(s1.compareTo((String)group.get(j)) == 0)
                                                {
                                                selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                    else if(org)
                                    {
                                        og=false;
                                        for (int j = 0; j < origin.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx3); 
                                                 s1=rec[idx3];
                                                if(s1.compareTo((String)origin.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(frm)
                                    {
                                        fm=false;
                                        for (int j = 0; j < form.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx4); 
                                                s1=rec[idx4];
                                                if(s1.compareTo((String)form.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(use1)
                                    {
                                        us=false;
                                        for (int j = 0; j < use.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx5); 
                                                s1=rec[idx5];
                                                if(s1.compareTo((String)use.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                      else if(ph_type)
                                    {
                                        ph=false;
                                        for (int j = 0; j < phototype.size(); j++) 
                                            {
                                                //s1= GetTaxaString(rec, idx6); 
                                                s1=rec[idx6];
                                                if(s1.compareTo((String)phototype.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                }
                    
                         }
                  
                          if(name.equals("phototype"))
                        {
                            for (int j = 0; j < phototype.size(); j++) 
                                {
                                    if(ph_type && !sub && !grp && !org && !frm && !use1 && !selectedph_type) 
                                        {
                                            // s1= GetTaxaString(rec, idx6); 
                                              s1=rec[idx6];
                                             if(s1.compareTo((String)phototype.get(j)) == 0)
                                                {
                                                selectedRow.add(rec);  
                                                }
                                         } 
                                    else if(ph_type && !sub && !grp && !org && !frm && !use1  && (selectedph_type)) 
                                         {
                                            // s1= GetTaxaString(rec, idx6);
                                             s1=rec[idx6];
                                             if (s1.compareTo((String)phototype.get(j)) == 0){
                                             selectedRow.add(rec);  
                                              }
                                         }
                                    else if(ph_type && selectedph_type && (sub || grp || org || frm || use1))
                                         {
                                             flag2=true;
                                            // s1= GetTaxaString(rec, idx6);
                                             s1=rec[idx6];
                                             if(s1.compareTo((String)phototype.get(j)) == 0){
                                             repeatSelection.add(rec);  
                                             }
                                            if(sub) sb=true;
                                            if(grp) gp=true;
                                            if(org) og=true;
                                            if(frm) fm=true;
                                            if(use1) us=true;
                                        }
                                    else 
                                        {
                                            flag=true;
                                            for(int i=0;i<selectedRow.size();i++)
                                                {    
                                                    String[] a=(String[])selectedRow.get(i);
                                                    //s2=GetTaxaString(a, idx6);
                                                    s2=a[idx6];
                                                    if(s2.compareTo((String)phototype.get(j)) == 0)
                                                    {
                                                     selectedRowcopy.add(a);
                                                    }
                                                }  
                                                selectedRow = new ArrayList();
                                                if(selectedRowcopy.size()>0)
                                                selectedRow=selectedRowcopy;
                                                selectedRowcopy = new ArrayList();
                                                break;
                                         }
                      
                                   } 
                                    if((phototype.size()==0)&&(sub || grp || frm || org || use1))
                                    {
                                    if(sub) sb=true;
                                    if(grp) gp=true;
                                    if(frm) fm=true;
                                    if(org) og=true;
                                    if(use1) us=true;
                                    flag3=true; 
                                    if(sub)
                                    {
                                        sb=false; 
                                        for (int j = 0; j < subject.size(); j++) 
                                          {
                                            //s1= GetTaxaString(rec, idx1); 
                                              s1=rec[idx1];
                                            if(s1.compareTo((String)subject.get(j)) == 0)
                                            {
                                              selectedRowcopy.add(rec);  
                                            }
                                          }
                                       
                                    }
                                    else if(grp)
                                    {
                                         gp=false;
                                        for (int j = 0; j < group.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx2); 
                                                s1=rec[idx2];
                                                if(s1.compareTo((String)group.get(j)) == 0)
                                                {
                                                selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                    else if(org)
                                    {
                                        og=false;
                                        for (int j = 0; j < origin.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx3); 
                                                s1=rec[idx3];
                                                if(s1.compareTo((String)origin.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(frm)
                                    {
                                        fm=false;
                                        for (int j = 0; j < form.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx4); 
                                                s1=rec[idx4];
                                                if(s1.compareTo((String)form.get(j)) == 0)
                                                {
                                                    selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                     else if(use1)
                                    {
                                        us=false;
                                        for (int j = 0; j < use.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx5);
                                                s1=rec[idx5];
                                                if(s1.compareTo((String)use.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                      else if(ph_type)
                                    {
                                        ph=false;
                                        for (int j = 0; j < phototype.size(); j++) 
                                            {
                                               // s1= GetTaxaString(rec, idx6); 
                                                s1=rec[idx6];
                                                if(s1.compareTo((String)phototype.get(j)) == 0)
                                                {
                                                  selectedRowcopy.add(rec);  
                                                }  
                                            }
                                    }
                                }
                    
                         }
                 if(flag)
                         {
                            break;
                         }
               }
             } 
             catch (IOException ec) {
                break;
             }
            
      }
     if((flag2)||(flag3))
          {
           
               if(flag3){repeatSelection=selectedRowcopy;
               selectedRowcopy= new ArrayList();
               }
               
              if(repeatSelection.size()!=0)
                  {
                      
                      if(sb)
                        {
                           for(int i=0;i<repeatSelection.size();i++)
                               {
                                    String[] a=(String[])repeatSelection.get(i); 
                                   // s2=GetTaxaString(a, idx1);
                                     s2=a[idx1];
                                    for(int k=0;k<subject.size();k++)
                                    {
                                        if(s2.compareTo((String)subject.get(k)) == 0)
                                            {
                                            selectedRowcopy.add(a);
                                            }
                                    }
                               } 
                           repeatSelection= new ArrayList();
                           if(selectedRowcopy.size()>0)
                           repeatSelection=selectedRowcopy;
                           selectedRowcopy = new ArrayList();
                        }
                        if(gp)
                        {
                           for(int i=0;i<repeatSelection.size();i++)
                                {
                                    String[] a=(String[])repeatSelection.get(i); 
                                   // s2=GetTaxaString(a, idx2);
                                    s2=a[idx2];
                                    for(int k=0;k<group.size();k++)
                                    {
                                        if(s2.compareTo((String)group.get(k)) == 0)
                                            {
                                              selectedRowcopy.add(a);
                                            }
                                    }
                               } 
                           repeatSelection= new ArrayList();
                           if(selectedRowcopy.size()>0)
                           repeatSelection=selectedRowcopy;
                           selectedRowcopy = new ArrayList();
                                                     
                        }
                     if(og)
                        {
                           for(int i=0;i<repeatSelection.size();i++)
                                {     
                                    String[] a=(String[])repeatSelection.get(i); 
                                    //s2=GetTaxaString(a, idx3);
                                     s2=a[idx3];
                                    for(int k=0;k<origin.size();k++)
                                        {
                                           if(s2.compareTo((String)origin.get(k)) == 0)
                                            {
                                            selectedRowcopy.add(a);
                                            }
                                        }
                                } 
                          repeatSelection= new ArrayList();
                          if(selectedRowcopy.size()>0)
                          repeatSelection=selectedRowcopy;
                          selectedRowcopy = new ArrayList();
                        }
                       if(fm)
                        {
                           for(int i=0;i<repeatSelection.size();i++)
                                {
                                    String[] a=(String[])repeatSelection.get(i); 
                                   // s2=GetTaxaString(a, idx4);
                                     s2=a[idx4];
                                    for(int k=0;k<form.size();k++)
                                      {
                                        if(s2.compareTo((String)form.get(k)) == 0)
                                        {
                                            selectedRowcopy.add(a);
                                        }
                                     }
                                } 
                           repeatSelection= new ArrayList();
                           if(selectedRowcopy.size()>0)
                           repeatSelection=selectedRowcopy;
                           selectedRowcopy = new ArrayList();
                             
                        }
                      if(us)
                        {
                           for(int i=0;i<repeatSelection.size();i++)
                                {
                                    String[] a=(String[])repeatSelection.get(i); 
                                    //s2=GetTaxaString(a, idx5);
                                      s2=a[idx5];
                                    for(int k=0;k<use.size();k++)
                                        {
                                            if(s2.compareTo((String)use.get(k)) == 0)
                                                {
                                                selectedRowcopy.add(a);
                                                }
                                        }
                                } 
                           repeatSelection= new ArrayList();
                           if(selectedRowcopy.size()>0)
                           repeatSelection=selectedRowcopy;
                           selectedRowcopy = new ArrayList();
                             
                        }
                       if(ph)
                        {
                           for(int i=0;i<repeatSelection.size();i++)
                                {
                                    String[] a=(String[])repeatSelection.get(i); 
                                    //s2=GetTaxaString(a, idx6);
                                     s2=a[idx6];
                                    for(int k=0;k<phototype.size();k++)
                                        {
                                            if(s2.compareTo((String)phototype.get(k)) == 0)
                                                {
                                                selectedRowcopy.add(a);
                                                }
                                        }
                                } 
                           repeatSelection= new ArrayList();
                           if(selectedRowcopy.size()>0)
                           repeatSelection=selectedRowcopy;
                           selectedRowcopy = new ArrayList();
                             
                        }
                   }
                        
                        selectedRow = new ArrayList();
                        if(repeatSelection.size()>0)
                        selectedRow = repeatSelection;
                        repeatSelection=new ArrayList();
                       
             }
         return selectedRow.size();
     }
*/
   public String[][] getFileNames2(ArrayList sel,String level, String[] taxa, boolean setEqual, int max) {
        
        if (taxa.length > max && max > 0) {
           
            String[] taxaTrimmed = new String[max];
            for (int i = 0; i < max; i++) {
                taxaTrimmed[i] = taxa[i];
            }
            taxa = taxaTrimmed;
        }
         
        ///////////////////////////////////////////////////////
        int idx = GetIndex(level);
        imageFileNames = new ArrayList();        //hold image file nameas
        taxonomy = new ArrayList();   //hold taxonomic names
        //BufferedReader buffer = myFileReader(); //Returns file reader
        String s;
      /*  if(level.equals("Genus")||(level.equals("Species")) ||(level.equals("Common Name"))){
             
        }*/
        Arrays.sort(taxa);
       
        //Reader header and do nothing
       
        int[] numImagesPerTaxa = new int[taxa.length];
      
        for(int i=0;i<sel.size();i++)
        {
                s = "";
                String snew = "";
                String[] rec=(String[])sel.get(i);
            
                s = GetTaxaString(rec, idx);
               
                snew = s;
                String[] b= s.split("#");
                if(b.length>1)
                    s = b[0];
                     
                for (int j = 0; j < taxa.length; j++) {
                     
                    if (s.compareTo(taxa[j]) == 0) {
                        numImagesPerTaxa[j]++;
                        //search index to insert item so list is alphabetized
                        //insert item at index k.
                        int k = 0;
                        while(true) {
                            if (taxonomy.size() == 0) {//Empty collection.
                                //first item here.
                               // taxonomy.add(s);
                              
                                 taxonomy.add(snew);
                                imageFileNames.add(rec[0]);
                                break;
                            } else if (k == taxonomy.size()) {// Not found...
                                //taxonomy.add(s);            // Insert at end.
                                taxonomy.add(snew);
                                imageFileNames.add(rec[0]);
                                
                                break;
                            } else if (taxonomy.get(k).toString().compareTo(s) > 0) {
                                //  Perform insertion as index k
                               // taxonomy.add(k, s);
                              
                                taxonomy.add(k, snew);
                                imageFileNames.add(k, rec[0]);
                                break;
                            } else {
                               
                                k++;
                            }
                        }
                    }
                }
         }
    
        if (max > 0) {
            TrimRecords(numImagesPerTaxa, imageFileNames, max);
        }
        if (setEqual == true) {
            EqualRecords(numImagesPerTaxa, taxa);
        }
      
        return AddFileExtension(imageFileNames, taxonomy);
    }
     
    public String[] getGenus2(String[] family) {
       // fm=family;
        BufferedReader buffer = myFileReader();
        SortedSet genusList = GetList2(buffer, family, GetIndex("Genus"), GetIndex("Family"));
        CloseBuffer(buffer);
        return treeToList(genusList);
    } 
     private SortedSet GetList2(BufferedReader buffer, String[] name, int idx, int parent) {
        String line = "";
        SortedSet lst = new TreeSet();
                
        //Reader header and do nothing
        try {
            line = buffer.readLine();
           
        } catch (IOException ec) {
            
        }
        String sep = ",";

        //Skip through all < Family name.
        try {
            line = buffer.readLine();
            
        } catch (IOException ec) {
        }
        if (line != null) {
            line = line.trim();
        }
         
        while (line != null) {

              //Extract all the ones where family matches
            try {
                String[] rec = line.split(sep);
                if(rec.length>4)
                {
                if (parent >= 0) {
                    lst = Extract2(lst, rec, name, parent, idx);
                } else {
                    lst = Extract2(lst, rec, idx);
                }
                }
            } catch (PatternSyntaxException ec) {
                break;
            } catch (Exception ec) {
                break;
            }
            try {
                line = buffer.readLine();
                
            } catch (IOException ec) {
                break;
            }
            if (line != null) {
                line = line.trim();
            }
        }
         
        return lst;
    }
      private SortedSet Extract2(SortedSet lst, String[] rec, String[] name, int parent, int idx) {
       for(int i=0;i<name.length;i++)
       {
        if (rec[parent].compareTo(name[i]) == 0) {
            
            if(withSubspecies) {
                if (rec[1].length() > 0) //check subspecies exists
                {
                    lst.add(rec[idx]);
                }
            } else {
                lst.add(rec[idx]);
                
            }
            if(idx==2)
             {
              if(!fm.contains(name[i]+"#"+rec[idx]))
               {
                fm.add(name[i]+"#"+rec[idx]);
               }
             }
         
            else if(idx==3)
             {
                if(!gen.contains(name[i]+"#"+rec[idx]))
                gen.add(name[i]+"#"+rec[idx]);
             }
            else if((idx==1)){
            if(!gen.contains(name[i]+"#"+rec[idx]+"#"+rec[GetIndex("Species")]))
                gen.add(name[i]+"#"+rec[idx]+"#"+rec[GetIndex("Species")]);
            }
        }
       }
        return lst;
    }

    private SortedSet Extract2(SortedSet lst, String[] rec, int idx) {
        if (withSubspecies) {
            if (rec[1].length() > 0) //check subspecies exists
            {
                lst.add(rec[idx]);
            }
        } else {
            
            lst.add(rec[idx]);
        }
        return lst;
    }
     public String[] getSpecies2(String[] genus) {
       // gen=genus;
        BufferedReader buffer = myFileReader();
        SortedSet speciesList = GetList2(buffer, genus, GetIndex("Species"), GetIndex("Genus"));
        CloseBuffer(buffer);
        return treeToList(speciesList);
    }
     public String[] getCommonName2(String[] genus)  {
      //  gen=genus;
        BufferedReader buffer = myFileReader();
        SortedSet speciesList = GetList2(buffer, genus, GetIndex("Common Name"), GetIndex("Genus"));
        CloseBuffer(buffer);
        return treeToList(speciesList);
    }
      private String GetTString(String[] record)
      {
        return(record[2] + " " + record[3]);   
      }
    /***/
       
   
     public String[] getFamily(String genus) {

        BufferedReader buffer = myFileReader();
        SortedSet familyList = GetList(buffer, genus, GetIndex("Family"), GetIndex("Genus"));
        CloseBuffer(buffer);
 
        
        return treeToList(familyList);
       }
  
  public  String[] getDataList(String it) {
      //  throw new UnsupportedOperationException("Not yet implemented");
       BufferedReader buffer = myFileReader();
        SortedSet GenusList = GetList(buffer, it, GetIndex("Genus"), GetIndex("common name 2"));
      
           return treeToList(GenusList);
    }

   
   
    public ArrayList getRows(String it,int idx){
        BufferedReader buffer = myFileReader();
        ArrayList rows=new ArrayList();
          String line,s1;
         // int idx=-15;
          
    try {
            line = buffer.readLine();
        } catch (IOException ec) {
            //TODO:  Something.
        }
   while (true) {
               try {
                    line = buffer.readLine();
                    if (line == null) {
                    break;
                     } else {
                    line = line.trim();
                    }
               
                    if(line.length()-1 == line.lastIndexOf(","))
                    {
                    line = line+"nill";
                    }
                                     
                    String[] rec = line.split(",");
                     if(rec.length>4)
                     {
                  //  s1= GetTaxaString(rec, idx);
                      s1=rec[idx];
                    
                    if(s1.equals(it)){
                    rows.add(rec);
                    }
                    }
                  } 
               catch (IOException ec) {
                 break;
                 }
             }
        return rows;
    }
    public String[] getTaxa2(String level,ArrayList sel) {
        int idx = GetIndex(level);
        
        SortedSet taxaList = GetListNew(sel, level, idx, -1);
       
        return treeToList(taxaList);
    }
     private SortedSet GetListNew(ArrayList sel, String name, int idx, int parent) {
           
         SortedSet lst = new TreeSet();
         for(int i=0;i<sel.size();i++)
        {
           
                String[] rec=(String[])sel.get(i);
                              
                if (parent >= 0) {
                    lst = Extract(lst, rec, name, parent, idx);
                } else {
                    lst = Extract(lst, rec, idx);
                }
            
        }
        
        return lst;
    }
   
private static String getField(String line) {
		return line.split(",")[1];// extract value you want to sort on } }
	}

 public String[][] getFileNames3(ArrayList sel,String level, String[] taxa, boolean setEqual, int max) {
        
        if (taxa.length > max && max > 0) {
           
            String[] taxaTrimmed = new String[max];
            for (int i = 0; i < max; i++) {
                taxaTrimmed[i] = taxa[i];
            }
            taxa = taxaTrimmed;
        }
         
        ///////////////////////////////////////////////////////
        int idx = GetIndex(level);
        imageFileNames = new ArrayList();        //hold image file nameas
        taxonomy = new ArrayList();   //hold taxonomic names
        
        String s;
      
        Arrays.sort(taxa);
       
        //Reader header and do nothing
       
        int[] numImagesPerTaxa = new int[taxa.length];
    
        Map<String, ArrayList> map = new TreeMap<String, ArrayList>();
        for(int i=0;i<sel.size();i++)
        {
                s = "";
                String snew = "";
                String[] rec=(String[])sel.get(i);
                String key = GetSortField(rec,idx);
                    key=key.toLowerCase();
                        ArrayList l=(ArrayList) map.get(key);
			if (l == null) {
				
                            l = new ArrayList();
				map.put(key, l);
			}
                       
			l.add(rec);
        }
      
         for (ArrayList list : map.values()) 
                   {
                                      
                           for (Object val : list) {
                               
                              String[] rec=(String[]) val; 
                           
                               s = "";
                               String snew = "";
                               s = GetTaxaString(rec, idx);
                               snew = s;
                               String[] b= s.split("#");
                               if(b.length>1)
                               s = b[0];
                               
                               for (int j = 0; j < taxa.length; j++) {
                                  if (s.compareTo(taxa[j]) == 0) {
                                  numImagesPerTaxa[j]++;
                                  taxonomy.add(s);
                                  imageFileNames.add(rec[0]);
                                  }
                                   
                               }
                                 
                           }
                           
                   }
                                   
    
        if (max > 0) {
           
            TrimRecords(numImagesPerTaxa, imageFileNames, max);
        }
        if (setEqual == true) {
            
            EqualRecords(numImagesPerTaxa, taxa);
        }
       
        return AddFileExtension(imageFileNames, taxonomy);
    }
  private String GetSortField(String[] record, int idx) {
   
       if (idx == 2) { 
        return record[2]; // code changes for alphabatical sorting for group selection
        }
       else if(idx==3){ 
        return record[2]+" "+record[3]; // code changes by anurag for species alphabatical images ordering 
       }else if(idx==4){
        return record[4];    
       }
       else
        return record[1];
     
  }
  
 public String[][] getFileNamesSearch(String level, String[] taxa, boolean setEqual, int max) {
        
        if (taxa.length > max && max > 0) {
           
            String[] taxaTrimmed = new String[max];
            for (int i = 0; i < max; i++) {
                taxaTrimmed[i] = taxa[i];
            }
            taxa = taxaTrimmed;
        }
         
        ///////////////////////////////////////////////////////
        int idx = GetIndex(level);
        imageFileNames = new ArrayList();        //hold image file nameas
        taxonomy = new ArrayList();   //hold taxonomic names
        
        String s;
        String line;
        BufferedReader buffer = myFileReader(); //Returns file reader
        Arrays.sort(taxa);
        //Reader header and do nothing
        try {
            line = buffer.readLine();
        } catch (IOException ec) {
            //TODO:  Something.
        }
        //Reader header and do nothing
       
        int[] numImagesPerTaxa = new int[taxa.length];
    
        Map<String, ArrayList> map = new TreeMap<String, ArrayList>();
          while (true) {
                s = "";
                String snew = "";
                  try {
                        line = buffer.readLine();
                if (line == null) {
                    break;
                } else {
                    line = line.trim();
                }
                String[] rec = line.split(",");
                 
                String key = GetSortField(rec,idx);
                    key=key.toLowerCase();
                        ArrayList l=(ArrayList) map.get(key);
                      if (l == null) {
				
                            l = new ArrayList();
				map.put(key, l);
			}
                       
			l.add(rec);
                  } catch (IOException ec) {
                break;
            }
          }
         for (ArrayList list : map.values()) 
                   {
                                      
                           for (Object val : list) {
                              
                               String[] rec=(String[]) val; 
                           
                               s = "";
                               String snew = "";
                               s = GetTaxaString(rec, idx);
                               snew = s;
                               String[] b= s.split("#");
                               if(b.length>1)
                               s = b[0];
                               
                               for (int j = 0; j < taxa.length; j++) {
                                  if (s.compareTo(taxa[j]) == 0) {
                                  numImagesPerTaxa[j]++;
                                  taxonomy.add(s);
                                  imageFileNames.add(rec[0]);
                                  }
                                   
                               }
                                 
                           }
                           
                   }
                                   
    
        if (max > 0) {
           
            TrimRecords(numImagesPerTaxa, imageFileNames, max);
        }
        if (setEqual == true) {
            
            EqualRecords(numImagesPerTaxa, taxa);
        }
      
        return AddFileExtension(imageFileNames, taxonomy);
    }
  public String[][] getGroupFileNames(ArrayList sel,String level,boolean setEqual, int max) {
        
     /*   if (taxa.length > max && max > 0) {
           
            String[] taxaTrimmed = new String[max];
            for (int i = 0; i < max; i++) {
                taxaTrimmed[i] = taxa[i];
            }
            taxa = taxaTrimmed;
        }*/
    
        ///////////////////////////////////////////////////////
        int idx = GetIndex(level);
        imageFileNames = new ArrayList();        //hold image file nameas
        taxonomy = new ArrayList();   //hold taxonomic names
         SortedSet txaList = new TreeSet();
        String s;
      
       // Arrays.sort(taxa);
       
        //Reader header and do nothing
       
      //  int[] numImagesPerTaxa = new int[taxa.length];
    
        Map<String, ArrayList> map = new TreeMap<String, ArrayList>();
        for(int i=0;i<sel.size();i++)
        {
                s = "";
                String snew = "";
                String[] rec=(String[])sel.get(i);
                String key = GetSortField(rec,idx);
                   key=key.toLowerCase(); 
                        ArrayList l=(ArrayList) map.get(key);
			if (l == null) {
				
                            l = new ArrayList();
				map.put(key, l);
			}
                       
			l.add(rec);
        }
        
        ArrayList<Integer> imageCount= new ArrayList<Integer>();
        int p=0;
        int count=0;
        boolean first=true;
         for (ArrayList list : map.values()) 
                   {
                  // Collections.sort(list);
                           for (Object val : list) {
                             
                               String[] rec=(String[]) val; 
                               s = "";
                               String snew = "";
                               s = GetTaxaString(rec, idx);
                               snew = s;
                               String[] b= s.split("#");
                               if(b.length>1)
                               s = b[0];
                               taxonomy.add(s);
                               imageFileNames.add(rec[0]);
                           
                               p++;
                              
                               if(!txaList.contains(s)){
                                  txaList.add(s);
                                 
                                  if(first)
                                  {
                                      first=false;
                                  }else{
                                  imageCount.add(p);
                                  
                                  p=0; }
                                  
                               }
                           }
                   }
         imageCount.add(p) ;
            int[] numImagesPerTaxa = new int[imageCount.size()];
         
           for(int c=0;c<imageCount.size();c++){
              numImagesPerTaxa[c]=(int) imageCount.get(c);
          }
        if(max > 0) {
           
            TrimRecords(numImagesPerTaxa, imageFileNames, max);
        }
      /*  if(setEqual == true) {
            
            EqualRecords(numImagesPerTaxa, taxa);
        }*/
        taxaList = treeToList(txaList); 
        
        return AddFileExtension(imageFileNames, taxonomy);
    }
   public ArrayList GetHeader() {
        String line = "";
        ArrayList header = new ArrayList();
         BufferedReader buffer = myFileReader();         
        //Reader header and do nothing
        try {
            line = buffer.readLine();
             String[] rec = line.split(",");
             int idx1=0,idx2=0;
             for(int i=0;i<rec.length;i++){
                 if(rec[i].equals("Family") || rec[i].equals("family")){
                    idx1=i; 
                 }
                 if(rec[i].equals("common name 2") || rec[i].equals("Common name 2")){
                    idx2=i; 
                 }
             }
             for(int i=idx1+1;i<idx2;i++){
                 header.add(rec[i]);
             }
              
        } catch (IOException ec) {
            
        }
        return header;
   }
 
}

