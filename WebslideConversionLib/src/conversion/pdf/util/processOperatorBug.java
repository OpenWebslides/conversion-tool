/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

/**
 *This class serves to suppres warnings of a missmatch between the used libraries 
 * @author Gertjan
 */

    	





import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class processOperatorBug extends PrintStream {

  public processOperatorBug() {
    super(new NullByteArrayOutputStream());
  }

  public static class NullByteArrayOutputStream extends ByteArrayOutputStream {

    @Override
    public void write(int b) {
      // do nothing
    }

    @Override
    public void write(byte[] b, int off, int len) {
      // do nothing
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
      // do nothing
    }

  }

}

