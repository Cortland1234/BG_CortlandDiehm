package edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm.databinding.ListItemGlucoseBinding
import java.text.SimpleDateFormat
import java.util.*

class GlucoseHolder(
    private val binding: ListItemGlucoseBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(glucose: Glucose, onGlucoseClicked: (glucoseID: Date) -> Unit) {
        var outputFormat = SimpleDateFormat("MM/dd/yyyy")
        var outputDate = outputFormat.format(glucose.date)

        binding.listDate.text = outputDate.toString()
        val average = (glucose.fasting + glucose.breakfast + glucose.lunch + glucose.dinner) / 4
        binding.listAverage.text = average.toString()

        binding.checkbox.isChecked =
            !(glucose.fasting < 70 || glucose.fasting > 100 || glucose.breakfast < 70 || glucose.breakfast > 140 || glucose.lunch < 70 || glucose.lunch > 140 || glucose.dinner < 70 || glucose.dinner > 140)

        binding.root.setOnClickListener {
            onGlucoseClicked(glucose.date)
        }
    }
}

class GlucoseListAdapter(
    private val glucoses: List<Glucose>,
    private val onGlucoseClicked: (glucoseID: Date) -> Unit
) : RecyclerView.Adapter<GlucoseHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlucoseHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGlucoseBinding.inflate(inflater, parent, false)
        return GlucoseHolder(binding)
    }

    override fun onBindViewHolder(holder: GlucoseHolder, position: Int) {
        val glucose = glucoses[position]
        holder.bind(glucose, onGlucoseClicked)
    }

    override fun getItemCount() = glucoses.size
}