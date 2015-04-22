package com.students.routingmstu;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POINT = "point";

    // TODO: Rename and change types of parameters
    private Point _point;
    SQLiteDatabase db;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POINT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // создаем объект для создания и управления версиями БД
            FeedReaderDbHelper reader = new FeedReaderDbHelper(getActivity());
            db = reader.getReadableDatabase();

            String pointName = getArguments().getString(ARG_POINT);
            if (pointName != null && pointName != "") {
                List<Point> points = GetPoints("ShortName = ?", new String[]{pointName});
                if (points != null && !points.isEmpty()) {
                    _point = points.get(0);
                }
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View searchView = inflater.inflate(R.layout.fragment_search, container, false);
        ListView listView = (ListView) searchView.findViewById(R.id.searchList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectItem(position);
            }
        });

        List<Point> points = GetPoints("IsImportant = ?", new String[] {"1"});
        listView.setAdapter(new ArrayAdapter<Point>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1,
                points));


        Button button = (Button) searchView.findViewById(R.id.searchButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String result = "Необходимо определить начальную точку";
                if (_point != null) {
                    Point endPoint = null;
                    ListView listView = (ListView) searchView.findViewById(R.id.searchList);
                    Object obj = listView.getSelectedItem();
                    if (obj != null) {
                        endPoint = (Point) obj;
                    } else {
                        EditText textViewCorpus = (EditText) searchView.findViewById(R.id.edit_corpus);
                        EditText textViewAdit = (EditText) searchView.findViewById(R.id.edit_audit);
                        String corpus =  textViewCorpus.getText().toString();
                        String audit = textViewAdit.getText().toString();

                        if (corpus != null && corpus != "" && audit != null && audit != "")
                        {

                                List<Point> points = GetPoints("ShortName = ?", new String[]{corpus + "-" + audit});
                                if (points != null && !points.isEmpty()) {
                                    endPoint = points.get(0);
                                }
                        }
                    }
                    if (endPoint != null)
                    {
                        //алгоритм Дейкстры
                    }
                    else
                    {
                        result = "Конечная точка отсутствует в базе данных";
                    }
                }

                if (mListener != null) {
                    mListener.onFragmentInteraction(result);
                }
            }
        });
        return searchView;
    }


private List<Point> GetPoints(String p_selection, String[] p_selectionArgs) {
    String[] projection = {
            FeedReaderContract.PointEntry._ID,
            FeedReaderContract.PointEntry.COLUMN_NAME_SHORTNAME,
    };
    Cursor c = db.query("Points", projection, p_selection, p_selectionArgs, null, null, null);

    List<Point> points = new ArrayList<Point>();
    if (c.moveToFirst()) {
        do {
            Point point = new Point();
            // определяем номера столбцов по имени в выборке
            point.Id = c.getInt(0);
            point.ShortName = c.getString(1);
            points.add(point);
        } while (c.moveToNext());
    }
    return points;
}


    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onFragmentInteraction("Кафедра САПР\n6-302\n6-301\nВыход на лестницу\n2 этажа вниз\nПереход в 3 корпус");
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String text);
    }

}
