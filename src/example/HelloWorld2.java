package example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.stage.Stage;


/**
 * Demo program, Oct 21, 2015
 * Daniel Huson
 */
public class HelloWorld2 extends Application{
    @Override
    public void init() throws Exception {
        System.err.println("init()");
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.err.println("start()");

        Pane pane = new Pane();

        Scene scene=new Scene(pane,400,400);

        Circle circle1 = new Circle(90,100,20);
        circle1.strokeProperty().setValue(Paint.valueOf("violet"));
        circle1.fillProperty().setValue(Paint.valueOf("transparent"));

        Circle circle2 = new Circle(110,100,20);
        circle1.strokeProperty().setValue(Paint.valueOf("yellow"));
        circle1.fillProperty().setValue(Paint.valueOf("transparent"));

        Rectangle rectangle = new Rectangle(80,80,40,40);
        rectangle.strokeProperty().set(Paint.valueOf("blue"));
        rectangle.strokeWidthProperty().setValue(20);
        rectangle.fillProperty().set(Paint.valueOf("transparent"));
        rectangle.rotateProperty().setValue(45.0);

        Line line1 = new Line(200,160,300,160);
        Line line2 = new Line(200,180,300,180);
        Line line3 = new Line(200,200,300,200);
        Line line4 = new Line(300,200,400,300);
        Line line5 = new Line(200,200,400,300);

//        StrokeLineJoin strokeLineJoin;
//        StrokeDashArray;
//        StrokeLineCap;
//        PolyLine
//        Polygon closes the polygon for you (3 lines open, 4th closing line for free)
//        Arc;
//        type: round chord open
//        QuadCurve;
//        CubicCurve; has two control points

        Shape shape = Shape.subtract(circle2, circle1);

        line1.strokeWidthProperty().setValue(20);
        line1.setStrokeLineCap(StrokeLineCap.ROUND);
        line3.strokeWidthProperty().setValue(20);
        line4.strokeWidthProperty().setValue(20);

        line3.strokeLineJoinProperty().setValue(StrokeLineJoin.MITER);
        line4.strokeLineJoinProperty().setValue(StrokeLineJoin.MITER);
        line5.strokeLineJoinProperty().setValue(StrokeLineJoin.MITER);

        Path path = new Path();
        Path close = new Path();
//        path.getElements().addAll(line3,line4,line5, close);

        pane.getChildren().addAll(rectangle, shape, circle1,line1,line2, line3,line4);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello world");

        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.err.println("stop()");
    }
}
