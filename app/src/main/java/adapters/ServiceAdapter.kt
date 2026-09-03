package com.example.pataservice.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pataservice.BookAppointmentActivity
import com.example.pataservice.R
import com.example.pataservice.models.Service

class ServiceAdapter (
    private val context: Context,
    private val services: List<Service>
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>(){

    class ServiceViewHolder(view: View) :
            RecyclerView.ViewHolder(view){
                val serviceName: TextView =
                    view.findViewById(R.id.tvServiceName)
                val providerName: TextView =
                    view.findViewById(R.id.tvProviderName)
                val category: TextView =
                    view.findViewById(R.id.tvCategory)
                val description: TextView =
                    view.findViewById(R.id.tvDescription)
                val price: TextView =
                    view.findViewById(R.id.tvPrice)
                val bookButton: Button =
                    view.findViewById(R.id.btnBookService)
            }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_service,
                parent,
                false
            )
        return ServiceViewHolder(view)
    }
    override fun onBindViewHolder(
        holder: ServiceViewHolder,
        position: Int
    ){
        val service = services[position]

        holder.serviceName.text =
            service.serviceName
        holder.providerName.text =
            "Provider: ${service.providerName}"
        holder.category.text =
            "category : ${service.category}"
        holder.description.text =
            service.description
        holder.price.text =
            "KES ${service.price}"
        holder.bookButton.setOnClickListener {
            val intent =
            Intent(
                context,
                BookAppointmentActivity::class.java
            )
            intent.putExtra(
                "serviceId",
                service.id
            )

            intent.putExtra(
                "providerId",
                service.providerId
            )

            intent.putExtra(
                "providerName",
                service.providerName
            )
            intent.putExtra(
                "serviceName",
                service.serviceName
            )
            intent.putExtra(
                "price",
                service.price
            )
            context.startActivity(intent)
        }
        }
    override fun getItemCount(): Int {
        return services.size
    }
}
