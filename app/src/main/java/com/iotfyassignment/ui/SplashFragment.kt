package com.iotfyassignment.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.iotfyassignment.R
import com.iotfyassignment.databinding.FragmentSplashBinding

class SplashFragment : Fragment(R.layout.fragment_splash) {

    lateinit var binding:FragmentSplashBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)
        startAnimation()
    }

    private fun startAnimation() {
        val animFade = AnimationUtils.loadAnimation(context, R.anim.animation_fade)
        binding.logo.startAnimation(animFade)
        animFade.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
               findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }
}