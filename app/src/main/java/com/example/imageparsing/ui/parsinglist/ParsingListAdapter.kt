package com.example.imageparsing.ui.parsinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imageparsing.R
import com.example.imageparsing.extention.afterTextChanged


class ParsingListAdapter constructor(private var parsingUrl: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    private lateinit var context: Context
    private var onHeaderClickListener: OnHeaderClickListener? = null
    private var onItemClickListener: OnItemClickListener? = null
    private var parsingList: List<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context = parent.context
        when (viewType) {
            TYPE_HEADER -> return ViewHolderHeader(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_parsing_header,
                    parent,
                    false
                )
            )
            TYPE_ITEM -> return ViewHolderItem(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_parsing,
                    parent,
                    false
                )
            )
        }
        return throw RuntimeException("there is no type that matches the type $viewType make sure your using types correctly")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderHeader -> {
                holder.btnParse.setOnClickListener {
                    onHeaderClickListener?.onParseButtonClicked()
                }
                holder.etParsingUrl.setText(parsingUrl)
                holder.etParsingUrl.afterTextChanged {
                    parsingUrl = it
                    onHeaderClickListener?.onParseTextChanged(it)
                }
            }
            is ViewHolderItem -> {
                val parsingUrl = parsingList?.get(position - 1)
                parsingUrl?.let {
                    holder.flParsingContainer.setOnClickListener {
                        onItemClickListener?.onItemClicked(parsingUrl)
                    }
                    holder.tvParsingUrl.text = parsingUrl
                }
            }
        }
    }

    override fun getItemCount() = parsingList?.size?.plus(1) ?: 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    fun setData(parsingList: List<String>) {
        this.parsingList = parsingList
        notifyDataSetChanged()
    }

    fun setOnHeaderClickListener(onHeaderClickListener: OnHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class ViewHolderHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etParsingUrl: EditText = itemView.findViewById(R.id.etParsingUrl);
        val btnParse: Button = itemView.findViewById(R.id.btnParse);
    }

    class ViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flParsingContainer: FrameLayout = itemView.findViewById(R.id.flParsingContainer);
        val tvParsingUrl: TextView = itemView.findViewById(R.id.tvParsingUrl);
    }

    interface OnHeaderClickListener {
        fun onParseButtonClicked()
        fun onParseTextChanged(text: String)
    }

    interface OnItemClickListener {
        fun onItemClicked(url: String)
    }

}