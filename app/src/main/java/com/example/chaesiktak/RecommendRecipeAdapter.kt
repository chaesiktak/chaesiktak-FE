import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.R
import com.example.chaesiktak.RecommendRecipe
import coil.load

class RecommendRecipeAdapter(private val recipeList: ArrayList<RecommendRecipe>) :
    RecyclerView.Adapter<RecommendRecipeAdapter.FoodViewHolder>() {

        var onItemClick : ((RecommendRecipe) -> Unit)? = null

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.home_recipe_card_imageview)
        val titleView: TextView = itemView.findViewById(R.id.recipe_card_title)
        val subtextView: TextView = itemView.findViewById(R.id.recipe_card_subtext)
        val likebtnView: ImageView = itemView.findViewById(R.id.scrapButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_recipe_card, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val recipe = recipeList[position]

        // Coil을 사용한 이미지 로딩 (null 처리)
        holder.imageView.load(recipe.image) {
            placeholder(R.drawable.placeholder_image)
            error(R.drawable.sample_image)
            fallback(R.drawable.sample_image)
        }

        holder.titleView.text = recipe.title
        holder.subtextView.text = recipe.subtext

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(recipe)
        }

        holder.likebtnView.setImageResource(if (recipe.isFavorite) R.drawable.likebutton_onclicked else R.drawable.likebutton)

        holder.likebtnView.setOnClickListener {
            recipe.isFavorite = !recipe.isFavorite
            notifyItemChanged(position)
        }
    }
}

