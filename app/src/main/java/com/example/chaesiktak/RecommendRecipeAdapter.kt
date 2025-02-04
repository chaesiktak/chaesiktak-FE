import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.R
import com.example.chaesiktak.RecommendRecipe

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
        holder.imageView.setImageResource(recipe.image)
        holder.titleView.text = recipe.title
        holder.subtextView.text = recipe.subtext

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(recipe)
        }

        // '좋아요' 버튼 상태 설정
        if (recipe.isFavorite) {
            holder.likebtnView.setImageResource(R.drawable.likebutton_onclicked)
        } else {
            holder.likebtnView.setImageResource(R.drawable.likebutton)
        }

        // '좋아요' 클릭 리스너
        holder.likebtnView.setOnClickListener {
            // 상태 토글
            recipe.isFavorite = !recipe.isFavorite

            // 하트 아이콘 변경
            if (recipe.isFavorite) {
                holder.likebtnView.setImageResource(R.drawable.likebutton_onclicked)
            } else {
                holder.likebtnView.setImageResource(R.drawable.likebutton)
            }

            // 상태 변경 후 해당 아이템만 새로고침
            notifyItemChanged(position)
        }
    }
}

