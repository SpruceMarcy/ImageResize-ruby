import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.regex.*;

class ImageResize{

    public ImageResize(){
    }

    public static void main(String args[]){
	if(!(( args.length == 6 && args[0].equals("resize") ) ||
	     ( args.length == 1 && args[0].equals("formats") ))){
		System.out.println("Proper usage:");
	    System.out.println("ImageResize resize in.jpg out.jpg 320 320 bicubic");
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
	    if(app.resize(args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]),args[5])){
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

    public boolean resize(String fname_in, String fname_out, int max_width, int max_height, String resize_type){
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

	int resize_type_int = AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
	switch (resize_type.toLowerCase()){
		case "nearest neighbor":
			resize_type_int=AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
			System.out.println("Using Nearest Neighbor");
			break;
		case "bilinear":
			resize_type_int=AffineTransformOp.TYPE_BILINEAR;
			System.out.println("Using Bilinear");
			break;
		case "bicubic":
			resize_type_int=AffineTransformOp.TYPE_BICUBIC;
			System.out.println("Using Bicubic");
			break;
		default:
			resize_type_int=AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
			System.out.println("Defaulting to Nearest Neighbor");
	}


	BufferedImage img_resized = new BufferedImage(width, height, img.getType());
	AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance((double)width / img.getWidth(), 
										       (double)height / img.getHeight()), 
						      resize_type_int);
	ato.filter(img, img_resized);
	

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