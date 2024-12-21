from api_helpers import (
    post_request,
    get_request,
    get_unique_id,
    authenticate_and_retrieve_token,
    get_headers_with_auth,
)
from datetime import datetime, timedelta
import random

user_data_list = [
    {
        "name": "Ali Farhan",
        "latitude": 77.62690248,
        "longitude": 12.97207241,
        "completeAddress": "123 Koramangala 5th Block, Bangalore, Karnataka 560034",
    },
    {
        "name": "John Doe",
        "latitude": 77.56923986,
        "longitude": 12.9444176,
        "completeAddress": "456 Indiranagar 100ft Road, Bangalore, Karnataka 560038",
    },
    {
        "name": "Jane Doe",
        "latitude": 77.57454031,
        "longitude": 12.96672048,
        "completeAddress": "789 Whitefield Main Road, Bangalore, Karnataka 560066",
    },
    {
        "name": "Alice Johnson",
        "latitude": 77.57411591,
        "longitude": 13.00444863,
        "completeAddress": "101 HSR Layout Sector 2, Bangalore, Karnataka 560102",
    },
    {
        "name": "Bob Smith",
        "latitude": 77.56641286,
        "longitude": 12.9970385,
        "completeAddress": "202 Hebbal Ring Road, Bangalore, Karnataka 560024",
    },
    {
        "name": "Charlie Brown",
        "latitude": 77.61113283,
        "longitude": 13.00860084,
        "completeAddress": "303 ITPL Main Road, Bangalore, Karnataka 560048",
    },
    {
        "name": "Diana Green",
        "latitude": 77.61378839,
        "longitude": 12.94903369,
        "completeAddress": "404 BTM Layout 2nd Stage, Bangalore, Karnataka 560076",
    },
    {
        "name": "Ethan White",
        "latitude": 77.56132982,
        "longitude": 12.94204481,
        "completeAddress": "505 Richmond Road, Bangalore, Karnataka 560025",
    },
    {
        "name": "Fiona Black",
        "latitude": 77.62419406,
        "longitude": 12.94370699,
        "completeAddress": "606 Electronic City Phase 1, Bangalore, Karnataka 560100",
    },
    {
        "name": "Gina Red",
        "latitude": 77.56857146,
        "longitude": 12.98824597,
        "completeAddress": "707 JP Nagar 7th Phase, Bangalore, Karnataka 560078",
    },
]


def setup_user(data={}):
    user_data = random.choice(user_data_list)
    user = {
        "fullName": f"{user_data['name']}",
        "email": f"{get_unique_id(user_data['name'])}@example.com",
        "password": "securepassword",
        "address": {
            "completeAddress": user_data["completeAddress"],
            "latitude": user_data["latitude"],
            "longitude": user_data["longitude"],
        },
    }
    user.update(data)
    response = post_request("auth/signup", user)
    retrieved_data = response.json()
    user_id = retrieved_data["id"]
    user_token = authenticate_and_retrieve_token(
        retrieved_data["email"], user["password"]
    )
    return user_id, user_token, retrieved_data

def setup_issue(user_token, data={}):
    issue = {
        "title": "Test Issue",
        "description": "This is a test issue",
        "categories": ["INFRASTRUCTURE", "PUBLIC_SAFETY"],
        "images": ["https://example.com/image1.jpg", "https://example.com/image2.jpg"],
        "status": "OPEN",
        "address": {
            "completeAddress": "123 Koramangala 5th Block, Bangalore, Karnataka 560034",
            "latitude": 77.62690248,
            "longitude": 12.97207241,
        },
    }
    issue.update(data)
    response = post_request("issues", issue, get_headers_with_auth(user_token)).json()
    return response["id"], response
