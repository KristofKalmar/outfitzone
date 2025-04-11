package com.kristofkalmar.outfitzone.ui.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kristofkalmar.outfitzone.R;
import com.kristofkalmar.outfitzone.adapter.NavigationListAdapter;
import com.kristofkalmar.outfitzone.databinding.FragmentMoreBinding;
import com.kristofkalmar.outfitzone.helper.XmlLoader;
import com.kristofkalmar.outfitzone.models.NavigationListItem;
import com.kristofkalmar.outfitzone.ui.components.authenticationcontainer.AuthenticationContainerFragment;

import java.util.List;

public class MoreFragment extends Fragment {

    private FragmentMoreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MoreViewModel moreViewModel =
                new ViewModelProvider(this).get(MoreViewModel.class);

        binding = FragmentMoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getActivity().setTitle(R.string.title_more);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.navigation_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<NavigationListItem> navItems = XmlLoader.loadNavigationItems(requireContext());

        NavigationListAdapter adapter = new NavigationListAdapter(navItems, getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(adapter);

        Fragment childFragment = new AuthenticationContainerFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_more_auth_container, childFragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}