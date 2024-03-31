package com.example.project1.ui.share

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project1.databinding.FragmentShareBinding

class ShareFragment : Fragment() {
    private var _binding: FragmentShareBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val shareshowViewModel =
            ViewModelProvider(this).get(ShareViewModel::class.java)

        _binding = FragmentShareBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textShare
        val imageViewLogo: ImageView = binding.imageLogo
        val buttonShare : Button = binding.btnShare

        shareshowViewModel.setText(resources.getString(com.example.project1.R.string.share_text))
        shareshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        shareshowViewModel.imageLogo.observe(viewLifecycleOwner) {
            imageViewLogo.setImageResource(it)
        }

        buttonShare.setOnClickListener {
            share()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun share() {
        val shareText = "Check out my travel memories from Spain provided by Tripify application!"
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)

        if (activity?.packageManager?.resolveActivity(shareIntent, 0) != null) {
            startActivity(shareIntent)
        } else {
            Toast.makeText(activity, "No app found to share", Toast.LENGTH_LONG).show()
        }
    }
}