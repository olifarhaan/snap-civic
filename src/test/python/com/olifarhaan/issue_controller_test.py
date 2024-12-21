from setup_helper import setup_user, setup_issue
from api_helpers import get_request, get_headers_with_auth
import random

issue_categories = [
    "INFRASTRUCTURE",
    "PUBLIC_SAFETY",
    "SANITATION_AND_CLEANLINESS",
    "ENVIRONMENT",
    "PUBLIC_UTILITIES",
    "COMMUNITY_SERVICES",
    "NOISE_AND_NUISANCE",
    "ACCESSIBILITY",
    "CULTURE_AND_HERITAGE",
    "OTHER",
]

statuses = ["OPEN", "IN_PROGRESS", "CLOSED"]


def test_create_issue():
    user_id, user_token, user_data = setup_user()
    issue_id, issue = setup_issue(user_token)
    issue_response = get_request(
        f"issues/{issue_id}", get_headers_with_auth(user_token)
    ).json()

    verify_issue(issue_response, issue)


def test_feed():
    user_id, user_token, user_data = setup_user()
    issues = []
    for _ in range(10):
        issue_id, issue = setup_issue(
            user_token,
            {
                "status": random.choice(statuses),
                "categories": random.sample(issue_categories, 2),
                "address": {
                    "completeAddress": "123 Koramangala 5th Block, Bangalore, Karnataka 560034",
                    "latitude": 77.62690248
                    + random.uniform(-0.01, 0.01),  # Simulating 1 km distance variation
                    "longitude": 12.97207241
                    + random.uniform(-0.01, 0.01),  # Simulating 1 km distance variation
                },
            },
        )
        issues.append((issue_id, issue))
    issue_response = get_request(
        f"issues/feed?distance=100&latitude=77.62690248&longitude=12.97207241",
        get_headers_with_auth(user_token),
    ).json()

    print(issue_response, len(issue_response))


def verify_issue(saved_issue, retrieved_issue):
    assert saved_issue["title"] == retrieved_issue["title"]
    assert saved_issue["description"] == retrieved_issue["description"]
    assert saved_issue["categories"] == retrieved_issue["categories"]
    assert saved_issue["images"] == retrieved_issue["images"]
    assert saved_issue["status"] == retrieved_issue["status"]
    assert saved_issue["address"] == retrieved_issue["address"]
    assert saved_issue["reportedBy"]["id"] == retrieved_issue["reportedBy"]["id"]
    assert saved_issue["createdAt"][:23] == retrieved_issue["createdAt"][:23]
    assert saved_issue["upvotes"] == retrieved_issue["upvotes"]
    assert saved_issue["comments"] == retrieved_issue["comments"]
    assert saved_issue["bookmarks"] == retrieved_issue["bookmarks"]
    assert saved_issue["distance"] == retrieved_issue["distance"]
