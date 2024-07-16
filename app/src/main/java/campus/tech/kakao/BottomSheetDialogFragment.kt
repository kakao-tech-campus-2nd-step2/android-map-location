package campus.tech.kakao.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import campus.tech.kakao.map.R

class PlaceInfoBottomSheet : BottomSheetDialogFragment() {

    private var placeName: String? = null
    private var roadAddressName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            placeName = it.getString("placeName")
            roadAddressName = it.getString("roadAddressName")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val placeNameTextView: TextView = view.findViewById(R.id.placeNameTextView)
        val roadAddressTextView: TextView = view.findViewById(R.id.roadAddressTextView)

        placeNameTextView.text = placeName
        roadAddressTextView.text = roadAddressName
    }

    companion object {
        @JvmStatic
        fun newInstance(placeName: String, roadAddressName: String) =
            PlaceInfoBottomSheet().apply {
                arguments = Bundle().apply {
                    putString("placeName", placeName)
                    putString("roadAddressName", roadAddressName)
                }
            }
    }
}