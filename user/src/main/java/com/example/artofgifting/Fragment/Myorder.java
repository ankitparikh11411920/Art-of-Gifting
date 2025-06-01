package com.example.artofgifting.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Adapters.OrderAdapter;
import com.example.artofgifting.Models.OrderModel;
import com.example.artofgifting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Myorder extends Fragment {
    private Toolbar toolbar;
    private RecyclerView order_recyclerview;
    private ArrayList<OrderModel> orderModelArrayList;

    //vars
    private File pdfFile;
    private OrderAdapter orderAdapter;
    private static final int STORAGE_CODE = 10;


    //firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.myorder, container, false);
        order_recyclerview=rootview.findViewById(R.id.user_recyclerview_order_history_myorder);
        inittoolbar();
        readorders();
        return rootview;
    }

    private void readorders() {
        order_recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false));
        orderModelArrayList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").child(user.getUid()).child("Orders").getChildren()) {
                    OrderModel orderModel = dataSnapshot1.getValue(OrderModel.class);
                    orderModelArrayList.add(orderModel);
                }
                orderAdapter = new OrderAdapter(getContext(), orderModelArrayList);
                order_recyclerview.setAdapter(orderAdapter);
                onsavebtnclick();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

}

    private void onsavebtnclick() {
        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onclick(final int position, View view) {


                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    //system OS>=MARSHMALLOW(6.0),check if permission is enabled or not
                    if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission is not granted,request it
                        String permissions = (Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        requestPermissions(new String[]{permissions}, STORAGE_CODE);
                    } else {
                        //permission already granted,call savepdf method
                        try {
                            savepdf(position);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    //system OS < MARSHMALLOW,call savepdf method
                    try {
                        savepdf(position);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void inittoolbar() {
        toolbar=getActivity().findViewById(R.id.home_toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
//                getFragmentManager().popBackStack();
            }
        });
    }

    private void savepdf(final int position) throws DocumentException, FileNotFoundException {
        FileOutputStream output;
        /*File mFilePath = new File(Environment.getExternalStorageDirectory() + "/artofgifting");
        if (!mFilePath.exists()) {
            mFilePath.mkdir();

        }*/
        String mFilename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String pdfname = Environment.getExternalStorageDirectory() + "/"  + mFilename + ".pdf"/* "Gift.pdf"*/;
//        pdfFile = new File(mFilePath.getAbsolutePath(), pdfname);
        output = new FileOutputStream(pdfname);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(4);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Name");
        table.addCell("Price");
        table.addCell("Qty");
        table.addCell("Grand Total");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.WHITE);
        }
        //get order data
        String prodname = orderModelArrayList.get(position).getProdname();
        String prodqty = orderModelArrayList.get(position).getOrderqty();
        String totalprice = orderModelArrayList.get(position).getFinalamount();
        String price= String.valueOf(Integer.parseInt(totalprice)/Integer.parseInt(prodqty));

        String currentdatetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis());
        table.addCell(prodname);
        table.addCell(price);
        table.addCell(prodqty);
        table.addCell(totalprice);
        PdfWriter.getInstance(document, output);
        //open doc
        document.open();
        // load image
       /* try {
            // get input stream
            int height=48;
            InputStream ims = getContext().getAssets().open("purplegiftbox.png");
            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.setHeight(height);
            bmp.setWidth(height);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            document.add(image);
        } catch (IOException ex) {
            return;
        }*/
        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.UNDEFINED, BaseColor.BLACK);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.BOLD, BaseColor.BLACK);
        document.add(new Paragraph("\n\nPRODUCT BILL\n\n", f));
        Paragraph paragraph = new Paragraph("Order Date & Time:" + orderModelArrayList.get(position).getOrderdate()+" "+orderModelArrayList.get(position).getOrdertime(), g);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(paragraph);
        Paragraph paragraph1 = new Paragraph("\nBilling Address:\n"+orderModelArrayList.get(position).getOrder_address()+"\n\n", g);
        paragraph1.setAlignment(Element.ALIGN_RIGHT);
        document.add(paragraph1);
        document.add(table);
        document.close();
        StyleableToast.makeText(getContext(), "Pdf Saved in" + pdfname, R.style.exampletoast).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted from popup, call save pdf method
                        readorders();

                } else {
                    //permission was denied from popup, show error message
                    StyleableToast.makeText(getContext(), "Permission Denied...!", R.style.exampletoast).show();
                }
            }
        }
    }


}
