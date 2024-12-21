package com.olifarhaan.model;

import java.time.LocalDate;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olifarhaan.request.UserRegistrationRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	private String fullName;

	@Column(unique = true)
	private String email;

	@JsonIgnore
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private String phoneNumber;

	@Valid
	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	private Address address;

	@NotNull
	@JsonIgnore
	private Point location;

	private LocalDate dateOfBirth;

	private String profilePictureUrl;

	public enum Role {
		ADMIN,
		USER
	}

	public enum Gender {
		MALE,
		FEMALE,
		OTHER,
		NOT_SPECIFIED
	}

	public User(UserRegistrationRequest request, String encodedPassword) {
		this.fullName = request.getFullName();
		this.email = request.getEmail();
		this.password = encodedPassword;
		this.role = Role.USER;
		this.gender = Gender.NOT_SPECIFIED;
		this.address = Address.builder()
				.completeAddress(request.getAddress().getCompleteAddress())
				.latitude(request.getAddress().getLatitude())
				.longitude(request.getAddress().getLongitude())
				.build();
	}

	@PrePersist
	@PreUpdate
	private void updateLocationPoint() {
		if (address.getLatitude() != null && address.getLongitude() != null) {
			GeometryFactory geometryFactory = new GeometryFactory();
			this.location = geometryFactory.createPoint(new Coordinate(address.getLongitude(), address.getLatitude()));
		}
	}
}
