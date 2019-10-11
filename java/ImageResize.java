import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.regex.*;

class ImageResize{

    public ImageResize(){
    }

    public static void main(String args[]){
	if(!(( args.length == 5 && args[0].equals("resize") ) ||
	     ( args.length == 1 && args[0].equals("formats") ))){
		System.out.println("Proper usage:");
	    System.out.println("ImageResize resize in.jpg out.jpg 320 320");
	    System.out.println("ImageResize formats");
	    String concatArgs = "";
		for (String arg:args) {
			concatArgs+=arg+" ";
		}
	    System.out.println("Your Args ("+args.length+") were:" + concatArgs);
	    System.exit(1);
	}
	ImageResize app = new ImageResize();
	if(args[0].equals("resize")){
	    if(app.resize(args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]))){
		System.out.println(args[2]);
	    }
	    else{
		System.out.println("error!");
	    }
	}
	else if(args[0].equals("formats")){
	    app.showFormats();
	}
    }

    public void showFormats(){
	System.out.print("read:");
	for(String name : ImageIO.getReaderFormatNames()){
	    System.out.print(name+",");
	}

	System.out.println("");
	
	System.out.print("write:");
	for(String name : ImageIO.getWriterFormatNames()){
	    System.out.print(name+",");
	}
    }

    public boolean resize(String fname_in, String fname_out, int max_width, int max_height){
	System.out.println(fname_in);
	BufferedImage img = null;
	try {
	    img = ImageIO.read(new File(fname_in));
	}
	catch (Exception e) {
	    e.printStackTrace();
	    img = null;
	}
	
	int width, height;
	if(img.getWidth() < img.getHeight()){
	    height = max_height;
	    width = img.getWidth() * max_height / img.getHeight();
	}
	else{
	    width = max_width;
	    height = img.getHeight() * max_width / img.getWidth();
	}
	System.out.println(img.getWidth() + "x" +img.getHeight() + " => " + width + "x" + height);

	BufferedImage img_resized = new BufferedImage(width, height, img.getType());

	Graphics2D g2d = img_resized.createGraphics();
	g2d.drawImage(img.getScaledInstance(width,height,java.awt.Image.SCALE_SMOOTH), 0, 0, width, height, null);
	g2d.dispose();
	g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	//AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance((double)width / img.getWidth(),
	//									       (double)height / img.getHeight()),
	//					      resize_type_int);
	//ato.filter(img, img_resized);
	

	String format = Pattern.compile("^.+\\.(.+)$").matcher(fname_out).replaceAll("$1");
	boolean result = false;
	try {
	    result = ImageIO.write(img_resized, format, new File(fname_out));
	}
	catch (Exception e) {
	    e.printStackTrace();
	    result = false;
	}
	return result;
    }
    
}