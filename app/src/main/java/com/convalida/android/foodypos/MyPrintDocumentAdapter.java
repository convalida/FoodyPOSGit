package com.convalida.android.foodypos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.util.Log;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MyPrintDocumentAdapter extends PrintDocumentAdapter {

    public PdfDocument pdfDocument;
    Context context;
    private int pageHeight;
    private int pageWidth;
    public int totalPages = 1;


    public void onStart(){
    super.onStart();
    Log.e("MyPrintDocumentAdapter ", "onStart called");
    }

    public MyPrintDocumentAdapter(Context context){
        this.context=context;
    }

    /**
     * This callback method is called after the call to the onStart() method and then again each time the user makes changes to the print settings (such as changing the orientation, paper size or color settings).
     * @param oldAttributes
     * @param newAttributes
     * @param cancellationSignal
     * @param callback
     * @param extras
     */
    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

        pdfDocument = new PrintedPdfDocument(context, newAttributes);
        pageHeight = newAttributes.getMediaSize().getHeightMils()/1000*72;
        pageWidth = newAttributes.getMediaSize().getWidthMils()/1000*72;
        if(cancellationSignal.isCanceled()) {
        callback.onLayoutCancelled();
        return;
        }
      //  int pages = computePageCount(newAttributes);

        if(totalPages>0){
          /**  PrintDocumentInfo info = new PrintDocumentInfo.Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(pages)
                    .build();
            callback.onLayoutFinished(info, true);**/
          PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("print_output.pdf")
                  .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                  .setPageCount(totalPages);
          PrintDocumentInfo info = builder.build();
          callback.onLayoutFinished(info, true);
        }else{
            callback.onLayoutFailed("Page count is zero");
        }
    }

    /**
     * This method is called after each call to onLayout() and is responsible for rendering the content on the canvases of the pages to be printed.
     * @param pageRanges
     * @param destination
     * @param cancellationSignal
     * @param callback
     */
    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        for(int i=0;i<totalPages;i++){
            if(pageInRange(pageRanges,i)){
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight,i).create();
                PdfDocument.Page page=pdfDocument.startPage(newPage);
                if(cancellationSignal.isCanceled()){
                    callback.onWriteCancelled();
                    pdfDocument.close();
                    pdfDocument=null;
                    return;
                }
                drawPage(page,i);
            }
        }
    }

    private void drawPage(PdfDocument.Page page, int pageNumber){
        Canvas canvas=page.getCanvas();
        pageNumber++;
        int titleBaseLine=72;
        int leftMargin=54;
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        canvas.drawText("This is some test content to verify that custom document printing works",leftMargin,titleBaseLine+35,paint);
       /** if(pageNumber%2==0)
            paint.setColor(Color.RED);
        else
            paint.setColor(Color.GREEN);
        PdfDocument.PageInfo pageInfo = page.getInfo();
        canvas.drawCircle(pageInfo.getPageWidth()/2,pageInfo.getPageHeight()/2,150,paint);**/
    }
    private boolean pageInRange(PageRange[] pageRanges, int page){
        for(int i=0;i<pageRanges.length;i++){
            if((page>=pageRanges[i].getStart())&&(page<=pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }

    private int computePageCount(PrintAttributes printAttributes) {
        return 1;
    }
}
