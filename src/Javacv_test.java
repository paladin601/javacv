import java.io.File;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import javax.swing.*;
import java.util.Scanner;
import javax.swing.filechooser.FileSystemView;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.OpenCVFrameConverter;


public class Javacv_test {

  public static void main(String[] args) throws FrameGrabber.Exception, InterruptedException {
    OpenCVFrameConverter.ToMat       matConv = new OpenCVFrameConverter.ToMat();
    Scanner read = new Scanner(System.in);
    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("coco.mkv");
    grabber.start();

    long lengthInTime= grabber.getLengthInTime();//microsegundos
    int lengthInVideoFrames= grabber.getLengthInVideoFrames();    
   
    CanvasFrame canvasFrame = new CanvasFrame("Extracted Frame", 1);
    canvasFrame.setCanvasSize(grabber.getImageWidth(), grabber.getImageHeight());
    canvasFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    Frame frame;
	//capturar frame exactamente de la mitad del video
    
    //1    
    for(int i=0;i<lengthInVideoFrames;i=i+1000){
        grabber.setVideoFrameNumber(i);
        frame=grabber.grab();
        canvasFrame.showImage(frame);
        if(i==6000){
            i=lengthInVideoFrames;
        }
    }   
    
    //2
    System.out.println("Ingresa la hora: ");
    long hour=read.nextLong();
    System.out.println("Ingresa los minutos: ");
    long min=read.nextLong();
    System.out.println("Ingresa los segundos: ");
    long sg=read.nextLong();
    hour=hour*60;
    min=(min+hour)*60;
    sg=(min+sg)*1000000;
    
    long result= (sg*lengthInVideoFrames)/lengthInTime;
    grabber.setVideoFrameNumber((int) result);//
    frame=grabber.grab();// frame converted
    
    canvasFrame.showImage(frame);
    
     //3
    save(matConv.convertToMat(frame).clone());
   
    
    
	//Tareas:
	//1-Capturar frame a distintos puntos del video utilizando 'getLengthInVideoFrames()'
	//2-Capturar frame en una hora-minuto-segundo especifico usando 'getLengthInTime()'
	//3-Convertir Frame a BufferedImage para desplegar o salvar a disco.

    grabber.release();
  }
  
  public static void save(Mat image){
        JFileChooser fChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        
        if( fChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            File fSelected = fChooser.getSelectedFile();
            
            //Guardar imagen
            opencv_imgcodecs.imwrite(fSelected.getAbsolutePath(), image);
            
        }
  }
  
  
}