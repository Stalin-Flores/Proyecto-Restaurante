package com.example.restaurante

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.restaurante.databinding.FragmentDetallesRecetaBinding

class DetallesRecetaFragment : Fragment() {

    private var _binding: FragmentDetallesRecetaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetallesRecetaBinding.inflate(inflater, container, false)
        return binding.root
    }

}