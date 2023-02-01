package edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.provider.Settings.System.DATE_FORMAT
import android.text.format.DateFormat.format
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm.databinding.FragmentGlucoseDetailBinding
import kotlinx.coroutines.launch
import java.lang.String.format
import java.text.DateFormat
import java.text.MessageFormat.format
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

private const val TAG = "DETAIL_FRAGMENT"

class GlucoseDetailFragment : Fragment() {

    private var _binding: FragmentGlucoseDetailBinding? = null
    private val binding
        get() = checkNotNull((_binding)) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private lateinit var glucose: Glucose

    private val args: GlucoseDetailFragmentArgs by navArgs()

    private val glucoseDetailViewModel: GlucoseDetailViewModel by viewModels {
        GlucoseDetailViewModelFactory(args.glucoseID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        glucose = Glucose()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_glucose_detail, menu)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentGlucoseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            fastEdittext.doOnTextChanged { text, _, _, _ ->
                fastEdittext.setSelection(fastEdittext.text.length)
                if (fastEdittext.length() == 0) {
                    glucose = glucose.copy(fasting = 0)
                    glucoseDetailViewModel.updateGlucose { oldGlucose -> oldGlucose.copy(fasting = 0) }
                 }
                else
                {
                    glucose = glucose.copy(fasting = text.toString().toInt())
                    glucoseDetailViewModel.updateGlucose { oldGlucose -> oldGlucose.copy(fasting = text.toString().toInt()) }
                }
                }
                bkEdittext.doOnTextChanged { text, _, _, _ ->
                    bkEdittext.setSelection(bkEdittext.text.length)
                    if (bkEdittext.length() == 0) {
                        glucose = glucose.copy(breakfast = 0)
                        glucoseDetailViewModel.updateGlucose { oldGlucose -> oldGlucose.copy(breakfast = 0) }
                    }
                    else
                    {
                        glucose = glucose.copy(breakfast = text.toString().toInt())
                        glucoseDetailViewModel.updateGlucose { oldGlucose -> oldGlucose.copy(breakfast = text.toString().toInt()) }
                    }

                }
                lunchEdittext.doOnTextChanged { text, _, _, _ ->
                    lunchEdittext.setSelection(lunchEdittext.text.length)
                    if (lunchEdittext.length() == 0) {
                        glucose = glucose.copy(lunch = 0)
                        glucoseDetailViewModel.updateGlucose { oldGlucose -> oldGlucose.copy(lunch = 0) }
                    }
                    else
                    {
                        glucose = glucose.copy(lunch = text.toString().toInt())
                        glucoseDetailViewModel.updateGlucose { oldGlucose -> oldGlucose.copy(lunch = text.toString().toInt()) }
                    }

                }
                dinnerEdittext.doOnTextChanged { text, _, _, _ ->
                    dinnerEdittext.setSelection(dinnerEdittext.text.length)
                    if (dinnerEdittext.length() == 0) {
                        glucose = glucose.copy(dinner = 0)
                        glucoseDetailViewModel.updateGlucose { oldGlucose -> oldGlucose.copy(dinner = 0) }
                    }
                    else {
                        glucose = glucose.copy(dinner = text.toString().toInt())
                        glucoseDetailViewModel.updateGlucose { oldGlucose -> oldGlucose.copy(dinner = text.toString().toInt()) }
                    }

                }

                fastEdittext.doAfterTextChanged {
                    if (glucose.fasting != null) {
                        if (glucose.fasting < 70 || glucose.fasting > 100) {
                            fastMessage.text = "Fasting: Abnormal!"
                        } else {
                            fastMessage.text = "Fasting: Normal"
                            dateMessage.text = Date().toString()
                        }
                    }
                }

                bkEdittext.doAfterTextChanged {
                    if (glucose.breakfast != null) {
                        if ( 70 < glucose.breakfast && glucose.breakfast < 140){
                            bkMessage.text = "Breakfast: Normal"
                        }
                        else {
                            bkMessage.text = "Breakfast: Abnormal!"
                            dateMessage.text = Date().toString()
                        }
                    }
                }

                lunchEdittext.doAfterTextChanged {
                    if ( glucose.lunch != null ) {
                        if ( 70 < glucose.lunch && glucose.lunch < 140){
                            lunchMessage.text = "Lunch: Normal"
                        }
                        else {
                            lunchMessage.text = "Lunch: Abnormal!"
                            dateMessage.text = Date().toString()
                        }
                    }
                }

                dinnerEdittext.doAfterTextChanged {
                    if (glucose.dinner != null) {
                        if ( 70 < glucose.dinner && glucose.dinner < 140){
                            dinnerMessage.text = "Dinner: Normal"
                        }
                        else {
                            dinnerMessage.text = "Dinner: Abnormal!"
                            dateMessage.text = Date().toString()
                        }
                    }
                }


            clearButton.setOnClickListener() {
                fastEdittext.setText("")
                bkEdittext.setText("")
                lunchEdittext.setText("")
                dinnerEdittext.setText("")
                fastMessage.text = " "
                bkMessage.text = " "
                lunchMessage.text = " "
                dinnerMessage.text = " "
                dateMessage.text = " "
            }

            histButton.setOnClickListener {
                it.findNavController().popBackStack()
                it.findNavController().navigate(R.id.glucoseListFragment)
            }

            }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                glucoseDetailViewModel.glucose.collect { glucose ->
                    glucose?.let { updateUi(it) }
                }
            }
        }

        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            val newDate =
                bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date

            if (glucoseDetailViewModel.getGlucose(newDate) == 0) {
                findNavController().navigate(
                    GlucoseDetailFragmentDirections.showGlucoseDetail(
                        newDate
                    )
                )
            }

        }


    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        private fun updateUi(glucose: Glucose) {
            binding.apply {
                val date = SimpleDateFormat("EEEE, MMMM dd, yyyy").format(glucose.date).toString()
                dateButton.text = date
                fastEdittext.setText(glucose.fasting.toString())
                bkEdittext.setText(glucose.breakfast.toString())
                lunchEdittext.setText(glucose.lunch.toString())
                dinnerEdittext.setText(glucose.dinner.toString())
                dateMessage.text = date

                dateButton.setOnClickListener {
                    findNavController().navigate(GlucoseDetailFragmentDirections.selectDate(glucose.date))
                }

                }

            }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.del_glucose -> {
                deleteEntry()
                true
            }
            R.id.send_glucose -> {
                sendEntry()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sendEntry() {
        val reportIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getGlucoseReport(glucose))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.glucose_report_subject))
        }
        val chooserIntent = Intent.createChooser(
            reportIntent,
            getString(R.string.send_report)
        )
        Log.d(TAG, getGlucoseReport(glucose).toString())
        startActivity(chooserIntent)
    }

    private fun deleteEntry() {
        viewLifecycleOwner.lifecycleScope.launch {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                glucoseDetailViewModel.deleteGlucose(args.glucoseID)
                Toast.makeText(requireContext(), "Successfully Removed Entry", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.glucoseListFragment)
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Delete Entry?")
            builder.setMessage("Are you sure you want to delete?")
            builder.create().show()
        }
    }

    private fun getGlucoseReport(glucose: Glucose): String {
        val dateString = SimpleDateFormat("EEEE, MMMM dd, yyyy").format(glucose.date).toString()
        return getString(R.string.glucose_report, dateString, glucose.fasting, glucose.breakfast, glucose.lunch, glucose.dinner)
    }
}

