package com.example.project1.ui.contactus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project1.databinding.FragmentContactusBinding

class ContacusFragment : Fragment() {

    private var _binding: FragmentContactusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val contactusViewModel =
            ViewModelProvider(this).get(ContactusViewModel::class.java)

        _binding = FragmentContactusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonEmail: Button = binding.btnEmail
        val textView: TextView = binding.textContactus
        val imageViewLogo = binding.imageLogo

        buttonEmail.setOnClickListener {
            composeEmail()
        }

        contactusViewModel.setText(resources.getString(com.example.project1.R.string.contact_text))
        contactusViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        contactusViewModel.imageLogo.observe(viewLifecycleOwner) {
            imageViewLogo.setImageResource(it)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun composeEmail() {
        val subject = "Feedback"
        val sendTo = "stefania.silivestru04@gmail.com"
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, sendTo)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)

        val shareIntent = Intent.createChooser(intent, "Send Email")
        if (activity?.packageManager?.resolveActivity(shareIntent, 0) != null) {
            startActivity(shareIntent)
        } else {
            Toast.makeText(activity, "No app found to share", Toast.LENGTH_LONG).show()
        }
    }
}