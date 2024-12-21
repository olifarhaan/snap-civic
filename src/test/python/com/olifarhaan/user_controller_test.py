from setup_helper import setup_user
from api_helpers import (
    get_request,
    get_headers_with_auth,
    post_request,
    delete_request,
    put_request,
    authenticate_and_retrieve_token,
)
import random
from setup_helper import user_data_list
import pytest


def test_create_user():
    user_id, user_token, user_data = setup_user()
    response = get_request("users/profile", get_headers_with_auth(user_token))
    assert response.status_code == 200
    verify_user_data(user_data, response.json())


def test_get_user():
    user_id, user_token, user_data = setup_user()
    response = get_request(f"users/{user_id}", get_headers_with_auth(user_token))
    assert response.status_code == 200
    verify_user_data(user_data, response.json())


def test_delete_user():
    user_id, user_token, user_data = setup_user()
    user_id1, user_token1, user_data1 = setup_user()

    # Trying to delete a user with different user token
    response = delete_request(
        f"users/{user_id}", get_headers_with_auth(user_token1), checkStatus=False
    )
    assert response.status_code == 403

    # Trying to get a user with different user token
    response = get_request(
        f"users/{user_id}", get_headers_with_auth(user_token1), checkStatus=False
    )
    assert response.status_code == 403

    response = delete_request(f"users/{user_id}", get_headers_with_auth(user_token))
    assert response.status_code == 204

    response = get_request(
        f"users/{user_id}", get_headers_with_auth(user_token), checkStatus=False
    )
    assert response.status_code == 401  # Since the token is invalid now


def test_update_user():
    user_id, user_token, user_data = setup_user()
    user_data = random.choice(user_data_list)
    data_to_update = {
        "fullName": user_data["name"],
        "gender": "MALE",
        "dateOfBirth": "2000-01-01",
        "phoneNumber": "1234567890",
        "address": {
            "completeAddress": user_data["completeAddress"],
            "latitude": user_data["latitude"],
            "longitude": user_data["longitude"],
        },
    }
    user_data.update(data_to_update)
    saved_response = put_request(
        f"users/{user_id}", data_to_update, get_headers_with_auth(user_token)
    ).json()
    retrieved_response = get_request(
        f"users/{user_id}", get_headers_with_auth(user_token)
    ).json()
    verify_user_data(saved_response, retrieved_response)


@pytest.mark.skip(
    reason="This test will fail while running from explorer. We have to run it from terminal and add the correct email"
)
def test_reset_password():
    email = input("Please enter the email: ")
    user_id, user_token, user_data = setup_user({"email": email})
    user_id1, user_token1, user_data1 = setup_user()
    new_password = random.randint(100000, 999999)
    response = post_request("auth/forgot-password?email=" + user_data["email"])
    token = input("Please enter the token for password reset: ")

    try:
        response = post_request(
            "auth/reset-password", {"token": token, "password": new_password}
        )
        assert response.status_code == 200
    except Exception as e:
        print(f"An error occurred during password reset: {e}")

    user_token = authenticate_and_retrieve_token(email, new_password)
    assert user_token is not None
    response = get_request("users/profile", get_headers_with_auth(user_token))
    verify_user_data(user_data, response.json())
    response = delete_request(
        f"users/{user_id}", get_headers_with_auth(user_token1), checkStatus=False
    )
    assert response.status_code == 403
    response = delete_request(f"users/{user_id}", get_headers_with_auth(user_token))
    response = get_request(
        f"users/{user_id}", get_headers_with_auth(user_token1), checkStatus=False
    )
    assert response.status_code == 404


def verify_user_data(saved_data, retrieved_data):
    assert saved_data["id"] == retrieved_data["id"]
    assert saved_data["fullName"] == retrieved_data["fullName"]
    assert saved_data["email"] == retrieved_data["email"]
    assert saved_data["role"] == retrieved_data["role"]
    assert saved_data["gender"] == retrieved_data["gender"]
    assert saved_data["phoneNumber"] == retrieved_data["phoneNumber"]
    assert saved_data["dateOfBirth"] == retrieved_data["dateOfBirth"]
    verify_address_data(saved_data["address"], retrieved_data["address"])


def verify_address_data(saved_address, retrieved_address):
    if saved_address and retrieved_address:
        assert saved_address["id"] == retrieved_address["id"]
        assert saved_address["completeAddress"] == retrieved_address["completeAddress"]
        assert saved_address["latitude"] == retrieved_address["latitude"]
        assert saved_address["longitude"] == retrieved_address["longitude"]
    elif saved_address is None and retrieved_address is None:
        assert True
    else:
        assert False


if __name__ == "__main__":
    test_reset_password()
