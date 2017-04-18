/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import objects.Image;
import objects.PPTObject;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

/**
 * This class overrides the processOperator in order to extract information about images.
 * @author Gertjan
 */
/*
Info in verband met nummering: nummers tellen voor elke afbeelding zoals die ontdekt wordt... 
-> de stream van de afbeeldingen krijgt dezelfde naam en zal langs een andere weg in de zip komen...
*/
public class getImageLocations extends PDFTextStripper {
    private static int imageNumber;
    private ArrayList<Image> images= new ArrayList<>();
    public getImageLocations() throws IOException {
        
        super();
        imageNumber = 0;
    }
    public int getImageNumber(){
        return imageNumber;
    }

    @Override
    protected void startPage(PDPage page) throws IOException {
        // process start of the page
        super.startPage(page);
    }
    /**
     * Fills the images arrayList with image objects found in arguments.
     * @param operator
     * @param arguments
     * @throws IOException 
     */
    @Override
    protected void processOperator(PDFOperator operator, List<COSBase> arguments) throws IOException {
        //super.processOperator(operator, arguments); //To change body of generated methods, choose Tools | Templates.

        if ("cm".equals(operator.getOperation())) {
            if (!arguments.isEmpty()) {
                float width = ((COSNumber) arguments.get(0)).floatValue();
                float height = ((COSNumber) arguments.get(3)).floatValue();
                float x = ((COSNumber) arguments.get(4)).floatValue();
                float y = ((COSNumber) arguments.get(5)).floatValue();
                // process image coordinates
                if(x!=0){    //hij vind ook afbeeldingen die geen afbeelding zijn... gekke objecten...
              //  System.out.println("afbeelding gevonden met hoogte: " + height + " breedte: " + width);
              //  System.out.println("gevonden op: " + x + "," + y);
                
                Image im = new Image();
                im.setFilename("img" + imageNumber + ".jpg");
                Dimension positie = new Dimension();
                Dimension afmeting = new Dimension();
                positie.setSize(x, y);
                afmeting.setSize(height, width);
                im.getLocation().setSize(positie);
                im.getDimension().setSize(afmeting);
                images.add(im);
                imageNumber++;
                
                }
                
            }
            super.processOperator(operator, arguments);
        }
        //super.processOperator(operator, arguments);
    }

    @Override
    protected void writeString(String text,
            List<TextPosition> textPositions) throws IOException {
        super.writeString(text, textPositions);
        /*not very shure if there's any use to this anymore...*/
    }

    public Collection<? extends PPTObject> getObjecten() {
        try{return images;}
        finally{
            images = new ArrayList<>();
        }
    }
   
}
