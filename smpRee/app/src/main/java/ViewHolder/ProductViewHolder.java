package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smpRee.R;

import Interface.ItemClickListner;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    public ItemClickListner iListner;

    public ProductViewHolder(@NonNull View itemView) {

        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.iListner = listner;
    }

    @Override
    public void onClick(View view) {

        iListner.onClick(view, getAdapterPosition(), false);
    }
}
