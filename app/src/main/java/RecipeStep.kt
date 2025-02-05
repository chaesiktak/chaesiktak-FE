import android.os.Parcel
import android.os.Parcelable

data class RecipeStep(
    val stepNumber: Int, // 1 -> Step 1
    val stepContent: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(stepNumber)
        parcel.writeString(stepContent)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<RecipeStep> {
        override fun createFromParcel(parcel: Parcel) = RecipeStep(parcel)
        override fun newArray(size: Int) = arrayOfNulls<RecipeStep?>(size)
    }
}
