package github.bluepsm.joytyapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import github.bluepsm.joytyapp.models.RequestModel;

public interface RequestRepository extends JpaRepository<RequestModel, Long> {
}
