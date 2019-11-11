package my.testapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import my.testapp.Interfaces.IClickListener;
import my.testapp.PersonActivity;
import my.testapp.R;
import my.testapp.model.Person;

public class AdapterPerson extends RecyclerView.Adapter<AdapterPerson.ViewHolder> implements IClickListener {

    private Context mcontext;
    private Person[] persons;

    public void updateUdapter(Person[] persons)
    {
        this.persons=persons;
        notifyDataSetChanged();
    }

    public AdapterPerson(Context context, Person[] persons) {
        this.persons = persons;
        mcontext=context;
    }
    @Override
    public AdapterPerson.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_rec, parent, false);
        return new ViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(AdapterPerson.ViewHolder holder, int position) {
        Person person = persons[position];
        ((TextView)holder.itemView.findViewById(R.id.name)).setText(person.sname+" "+person.name);
        ((TextView)holder.itemView.findViewById(R.id.gender)).setText("Пол: "+person.gender+"  Возраст: "+person.age);
    }

    @Override
    public int getItemCount() {
        return persons.length;
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(mcontext, PersonActivity.class);
        intent.putExtra("person", persons[position]);
         mcontext.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        IClickListener listener;

        ViewHolder(View view,IClickListener listener){
            super(view);
            this.listener=listener;
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listener.onClick(getAdapterPosition());
        }
    }
}