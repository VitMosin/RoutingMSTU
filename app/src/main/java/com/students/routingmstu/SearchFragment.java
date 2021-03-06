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

import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
    private Point _selectedPoint;
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
                _selectedPoint = (Point) parent.getItemAtPosition(position);
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
                    Point endPoint = _selectedPoint;
                    if (endPoint == null) {
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
                        Point startPoint = null;

                        //проставляем начальные условия
                        List<Point> allPoints = GetPoints("1 = 1", null);
                        List<Length> allLengthes = GetLengthes("1 = 1", null);
                        for(Iterator<Point> i = allPoints.iterator(); i.hasNext(); ) {
                            Point item = i.next();
                            if (item.Id == endPoint.Id)
                            {
                                endPoint = item;
                            }
                            if (item.Id == _point.Id)
                            {
                                startPoint = item;
                                startPoint.Weight = 0;
                            }
                            else {
                                item.Weight = 100;
                            }
                        }

                        Point minPoint = ExistNoVisitedPoint(allPoints);
                        while (minPoint != null)
                        {
                            minPoint.IsVisited = true;
                            MarkLinkedPoints(minPoint, allPoints, allLengthes);

                            minPoint = ExistNoVisitedPoint(allPoints);
                        }
                        result = endPoint.Route;
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



    private void MarkLinkedPoints(Point p_point, List<Point> p_allPoints, List<Length> p_allLengthes) {
        for(Iterator<Point> i = p_allPoints.iterator(); i.hasNext(); ) {
            Point item = i.next();
            if (!item.IsVisited)
            {
                MarkLinkedPoint(p_point, item, p_allLengthes);
            }
        }
    }



    private void MarkLinkedPoint(Point p_pointFrom, Point p_pointTo, List<Length> p_allLengthes) {
        for(Iterator<Length> i = p_allLengthes.iterator(); i.hasNext(); ) {
            Length length = i.next();
            if (length.StartPointId == p_pointFrom.Id && length.EndPointId == p_pointTo.Id
                    || length.EndPointId == p_pointFrom.Id && length.StartPointId == p_pointTo.Id)
            {
                if (p_pointTo.Weight > p_pointFrom.Weight + length.Length)
                {
                    p_pointTo.Weight = p_pointFrom.Weight + length.Length;
                    p_pointTo.Route = p_pointFrom.Route + "\n" + p_pointTo.ShortName + "\n";
                }
            }
        }
    }


    private Point ExistNoVisitedPoint(List<Point> p_allPoints) {
        Point result = null;
        for(Iterator<Point> i = p_allPoints.iterator(); i.hasNext(); ) {
            Point item = i.next();
            if (!item.IsVisited && (result == null || result.Weight > item.Weight ))
            {
                result = item;
            }
        }
        return result;
    }


    private List<Point> GetPoints(String p_selection, String[] p_selectionArgs) {
        String[] projection = {
                FeedReaderContract.PointEntry._ID,
                FeedReaderContract.PointEntry.COLUMN_NAME_SHORTNAME,
        };
        Cursor c = db.query(FeedReaderContract.PointEntry.TABLE_NAME, projection, p_selection, p_selectionArgs, null, null, null);

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


    private List<Length> GetLengthes(String p_selection, String[] p_selectionArgs) {
        String[] projection = {
                FeedReaderContract.LengthEntry._ID,
                FeedReaderContract.LengthEntry.COLUMN_NAME_STARTPOINTID,
                FeedReaderContract.LengthEntry.COLUMN_NAME_ENDPOINTID,
                FeedReaderContract.LengthEntry.COLUMN_NAME_LENGTH
        };
        Cursor c = db.query(FeedReaderContract.LengthEntry.TABLE_NAME, projection, p_selection, p_selectionArgs, null, null, null);

        List<Length> lengthes = new ArrayList<Length>();
        if (c.moveToFirst()) {
            do {
                Length length = new Length();
                length.Id = c.getInt(0);
                length.StartPointId = c.getInt(1);
                length.EndPointId = c.getInt(2);
                length.Length = c.getInt(3);
                lengthes.add(length);
            } while (c.moveToNext());
        }
        return lengthes;
    }


    public void SetStartPoint(String p_pointName)
    {
        if (p_pointName != null && p_pointName != "") {
            List<Point> points = GetPoints("ShortName = ?", new String[]{p_pointName});
            if (points != null && !points.isEmpty()) {
                _point = points.get(0);
            }
        }
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
