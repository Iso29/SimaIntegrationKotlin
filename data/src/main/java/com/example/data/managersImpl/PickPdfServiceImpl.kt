package com.example.data.managersImpl

import android.content.Intent
import com.example.data.services.PickPDfService


class PickPdfServiceImpl() : PickPDfService {
    override fun startPickIntent():Intent?{
        return Intent(Intent.ACTION_OPEN_DOCUMENT).setType("application/pdf")
    }

}