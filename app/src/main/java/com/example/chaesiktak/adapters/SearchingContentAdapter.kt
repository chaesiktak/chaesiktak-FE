import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chaesiktak.R
import com.example.chaesiktak.RecommendRecipe

class SearchingContentAdapter(private var searchingContentList: MutableList<RecommendRecipe>) :
    RecyclerView.Adapter<SearchingContentAdapter.FoodViewHolder>() {

    var onItemClick: ((RecommendRecipe) -> Unit)? = null // 클릭 리스너

    // ViewHolder 정의 (이미지, 타이틀, isFavorite, 레시피 미리보기, 태그)
    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.searching_content_img)
        val titleView: TextView = itemView.findViewById(R.id.searching_content_title)
        val prevtextView: TextView = itemView.findViewById(R.id.searching_content_prevtext)
        val favorView: ImageView = itemView.findViewById(R.id.searching_content_isfavorite)
        val tagView: TextView = itemView.findViewById(R.id.searching_content_tag) // 태그 View 추가
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.searching_content, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchingContentList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val recipe = searchingContentList[position]

        // Coil을 사용한 이미지 로딩 (null 처리)
        holder.imageView.load(recipe.image) {
            placeholder(R.drawable.placeholder_image)
            error(R.drawable.placeholder_image)
            fallback(R.drawable.placeholder_image)
        }

        holder.titleView.text = recipe.title
        holder.prevtextView.text = recipe.prevtext

        // 클릭 리스너 추가
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(recipe) // 클릭된 레시피 객체 전달
        }

        // '좋아요' 버튼 상태 설정
        holder.favorView.setImageResource(
            if (recipe.isFavorite) R.drawable.likebutton_onclicked else R.drawable.likebutton
        )

        // '좋아요' 클릭 리스너
        holder.favorView.setOnClickListener {
            recipe.isFavorite = !recipe.isFavorite
            notifyItemChanged(position) // 상태 변경 후 해당 아이템만 새로고침
        }

        // 태그 설정 및 색상 변경
        holder.tagView.text = recipe.tag
        holder.tagView.setBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, getTagColor(recipe.tag))
        )
    }

    // 태그에 맞는 색상 반환
    private fun getTagColor(tag: String): Int {
        return when (tag) {
            "비건" -> R.color.color_vegan
            "락토" -> R.color.color_lacto
            "오보" -> R.color.color_ovo
            "락토오보" -> R.color.color_lacto_ovo
            "페스코" -> R.color.color_pesco
            else -> R.color.color_pollo
        }
    }

    // 검색 결과 업데이트 메서드
    fun updateList(newList: List<RecommendRecipe>) {
        searchingContentList.clear()
        searchingContentList.addAll(newList)
        notifyDataSetChanged()
    }

    //리스트의 getItemCount()
    fun getCurrentListsize():Int{
        return searchingContentList.size
    }


}
