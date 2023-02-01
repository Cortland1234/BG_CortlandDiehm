package edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm.databinding.GlucoseListFragmentBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "LIST_FRAGMENT"

class GlucoseListFragment: Fragment() {

    private var _binding: GlucoseListFragmentBinding? = null
    private val binding
        get() = checkNotNull((_binding)) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val glucoseListViewModel: GlucoseListViewModel by viewModels()

    private val args: GlucoseDetailFragmentArgs by navArgs()

    private val glucoseDetailViewModel: GlucoseDetailViewModel by viewModels {
        GlucoseDetailViewModelFactory(args.glucoseID)
    }

    val newGlucose = Glucose(
        fasting = 0,
        breakfast = 0,
        lunch = 0,
        dinner = 0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_glucose_list, menu)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            GlucoseListFragmentBinding.inflate(inflater, container, false)

        binding.glucoseRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                glucoseListViewModel.glucoses.collect { glucoses ->
                    binding.glucoseRecyclerView.adapter =
                        GlucoseListAdapter(glucoses) { glucoseID ->
                            findNavController().navigate(
                                GlucoseListFragmentDirections.showGlucoseDetail(glucoseID)
                            )
                        }
                }
            }
        }
        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            val newDate =
                bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date

            glucoseDetailViewModel.updateGlucose {
                newGlucose.copy(date = newDate)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_glucose -> {
                showNewGlucose()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showNewGlucose() {


        viewLifecycleOwner.lifecycleScope.launch {


            findNavController().navigate(GlucoseDetailFragmentDirections.selectDate(newGlucose.date))
            glucoseListViewModel.addGlucose(newGlucose)
            findNavController().navigate(GlucoseDetailFragmentDirections.showGlucoseDetail(newGlucose.date))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}