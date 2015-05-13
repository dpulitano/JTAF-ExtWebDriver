/*
 * (C) Copyright 2013 Java Test Automation Framework Contributors.
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
 * 
 */

package org.finra.jtaf.ewd.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.WidgetException;

/***
 * To compare screenshots of elements
 * 
 * @author Pulitand
 *
 */
public class ScreenshotComparator {

	private static Logger log = LoggerFactory.getLogger(ScreenshotComparator.class);
	
	/***
	 * Prereq: The page on which you are taking the screenshot is fully loaded
	 * 
	 * Take a screenshot of the element identified by element and save the file as toSaveAs
	 * (Note that this file should be saved as a png).
	 * Test that the control picture, controlPicture, is both the same size as,
	 * and, has a similarity value greater than or equal to the threshold.
	 * 
	 * @param element - the element to be tested
	 * @param controlPicture - the file of the picture that will serve as the control
	 * @param toSaveTestPictureAs - for example, save the file at "testData/textFieldWidget/screenshot.png"
	 * @param threshold - you are asserting that the similarity between the two pictures
	 * is a double greater than or equal to this double (between 0.0 and 1.0)
	 * @throws Exception 
	 */
	public static boolean isElementSimilarToScreenshot(IElement element, File controlPicture, File
			toSaveTestPictureAs, double threshold) throws IOException, WidgetException {
		
		element.captureElementScreenshot(toSaveTestPictureAs);
		
		log.info("Screenshot was successful. Comparing against control...");

		BufferedImage var = ImageIO.read(toSaveTestPictureAs);
		BufferedImage cont = ImageIO.read(controlPicture);
		
		return isSimilar(var, cont, threshold);
	}
	
	/***
	 * Prereq: The page on which you are taking the screenshot is fully loaded
	 *
	 * Take a screenshot of the element identified by element and save the file as toSaveAs
	 * (Note that this file should be saved as a png).
	 * Test that the control picture, controlPicture, is both the same size as,
	 * and, has a similarity value greater than or equal to the default threshold of .85.
	 * 
	 * @param element - the element to be tested
	 * @param controlPicture - the file of the picture that will serve as the control
	 * @param toSaveTestPictureAs - for example, save the file at "testData/textFieldWidget/screenshot.png"
	 * @throws WidgetException 
	 * @throws IOException 
	 * @throws Exception 
	 */
	public static boolean isElementSimilarToScreenshot(IElement element, File controlPicture, 
			File toSaveTestPictureAs) throws IOException, WidgetException {
		return isElementSimilarToScreenshot(element, controlPicture, toSaveTestPictureAs, .85);
	}
	
	private static boolean isSimilar(BufferedImage var, BufferedImage cont, double threshold) {
		return similarity(var, cont) >= threshold;
	}
	
	//Returns a double between 0 and 1.0
	private static double similarity(BufferedImage var, BufferedImage cont) {
		
		double[] varArr = new double[var.getWidth()*var.getHeight()*3];
		double[] contArr = new double[cont.getWidth()*cont.getHeight()*3];
		
		if (varArr.length!=contArr.length)
			throw new IllegalStateException("The pictures are different sizes!");
		
		//unroll pixels
		for(int i = 0; i < var.getHeight(); i++) {
			for(int j = 0; j < var.getWidth(); j++) {
				varArr[i*var.getWidth() + j + 0] = new Color(var.getRGB(j, i)).getRed();
				contArr[i*cont.getWidth() + j + 0] = new Color(cont.getRGB(j, i)).getRed();
				varArr[i*var.getWidth() + j + 1] = new Color(var.getRGB(j, i)).getGreen();
				contArr[i*cont.getWidth() + j + 1] = new Color(cont.getRGB(j, i)).getGreen();
				varArr[i*var.getWidth() + j + 2] = new Color(var.getRGB(j, i)).getBlue();
				contArr[i*cont.getWidth() + j + 2] = new Color(cont.getRGB(j, i)).getBlue();
			}
		}

		double mins=0;
		double maxs=0;
		for(int i=0;i!=varArr.length;i++){
			if (varArr[i]>contArr[i]){
				mins+=contArr[i];
				maxs+=varArr[i];
			} else {
				mins+=varArr[i];
				maxs+=contArr[i];
			}
		}
		return mins/maxs;
	}


}
