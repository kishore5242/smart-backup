package com.kishore.sb.controller;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kishore.sb.model.FolderSelection;

@Controller
public class WelcomeController {

	@Value("${welcome.message}")
	private String message;

	private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

	@GetMapping("/")
	public String main(Model model) {
		model.addAttribute("message", message);
		model.addAttribute("tasks", tasks);		
		model.addAttribute("folderSelection", new FolderSelection());
		
		return "welcome"; // view
	}

	@GetMapping("/hello")
	public String mainWithParam(
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			Model model) {

		model.addAttribute("message", name);
		return "welcome"; // view
	}
	
	@PostMapping("/review")
	public String review(@ModelAttribute FolderSelection folderSelection, Model model) {
		
	    List<String> paths = new ArrayList<String>();  
		
	      //Creating a File object for directory
	      File directoryPath = new File(folderSelection.getSourceFolder());
	      //List of all files and directories
	      File filesList[] = directoryPath.listFiles();
	      System.out.println("List of files and directories in the specified directory:");
	      for(File file : filesList) {
	         System.out.println("File name: "+file.getName());
	         System.out.println("File path: "+file.getAbsolutePath());
	         System.out.println("Size :"+file.getTotalSpace());
	         System.out.println(" ");
	         paths.add(file.getName());
	         saveFileThumbnail(file);
	      }

		model.addAttribute("tasks", paths);	
		
		return "review"; // view
	}

	private void saveFileThumbnail(File file) {
		BufferedImage originalBufferedImage = null;
		try {
		    originalBufferedImage = ImageIO.read(file);
		    
		    
		    int thumbnailWidth = 150;
		    
		    int widthToScale, heightToScale;
		    if (originalBufferedImage.getWidth() > originalBufferedImage.getHeight()) {
		     
		        heightToScale = (int)(1.1 * thumbnailWidth);
		        widthToScale = (int)((heightToScale * 1.0) / originalBufferedImage.getHeight() 
		                        * originalBufferedImage.getWidth());
		     
		    } else {
		        widthToScale = (int)(1.1 * thumbnailWidth);
		        heightToScale = (int)((widthToScale * 1.0) / originalBufferedImage.getWidth() 
		                        * originalBufferedImage.getHeight());
		    }
		    
		    
		    
		    
		    BufferedImage resizedImage = new BufferedImage(widthToScale, 
		    	    heightToScale, originalBufferedImage.getType());
		    	Graphics2D g = resizedImage.createGraphics();
		    	 
		    	g.setComposite(AlphaComposite.Src);
		    	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    	g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    	 
		    	g.drawImage(originalBufferedImage, 0, 0, widthToScale, heightToScale, null);
		    	g.dispose();
		    	
		    	
		    	
		    	int x = (resizedImage.getWidth() - thumbnailWidth) / 2;
		    	int y = (resizedImage.getHeight() - thumbnailWidth) / 2;
		    	 
		    	if (x < 0 || y < 0) {
		    	    throw new IllegalArgumentException("Width of new thumbnail is bigger than original image");
		    	}
		    	
		    	BufferedImage thumbnailBufferedImage = resizedImage.getSubimage(x, y, thumbnailWidth, thumbnailWidth);
		    	
		    	
		    	ImageIO.write(thumbnailBufferedImage, "JPG", new File("thumbnails/"+file.getName()));
		    
		}   
		catch(IOException ioe) {
		    System.out.println("IO exception occurred while trying to read image.");
		    ioe.printStackTrace();
		}
		
	}

}