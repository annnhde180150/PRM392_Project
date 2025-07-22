{{baseUrl}}/api/Bookings/ActiveByUser/:userId

response:
{
    "success": true,
    "statusCode": 200,
    "message": "Thành công",
    "data": [
        {
            "bookingId": 6,
            "requestId": 9,
            "userId": 1,
            "serviceId": 1,
            "addressId": 1,
            "status": "Completed",
            "scheduledStartTime": "2025-07-25T15:02:00",
            "scheduledEndTime": "2025-07-25T17:02:00",
            "latitude": null,
            "longitude": null,
            "ward": "Ben Nghe",
            "district": "District 1",
            "city": "Ho Chi Minh City",
            "fullAddress": "123 Le Loi, Ben Nghe, District 1, HCMC",
            "fullName": "Minhaza",
            "estimatedPrice": 20.00,
            "serviceName": "Cleaning"
        },
        {
            "bookingId": 7,
            "requestId": 10,
            "userId": 1,
            "serviceId": 1,
            "addressId": 1,
            "status": "InProgress",
            "scheduledStartTime": "2025-07-26T15:12:00",
            "scheduledEndTime": "2025-07-26T17:12:00",
            "latitude": null,
            "longitude": null,
            "ward": "Ben Nghe",
            "district": "District 1",
            "city": "Ho Chi Minh City",
            "fullAddress": "123 Le Loi, Ben Nghe, District 1, HCMC",
            "fullName": "Minhaza",
            "estimatedPrice": 20.00,
            "serviceName": "Cleaning"
        },
        {
            "bookingId": 8,
            "requestId": 11,
            "userId": 1,
            "serviceId": 1,
            "addressId": 1,
            "status": "InProgress",
            "scheduledStartTime": "2025-07-30T17:06:00",
            "scheduledEndTime": "2025-07-30T19:06:00",
            "latitude": null,
            "longitude": null,
            "ward": "Ben Nghe",
            "district": "District 1",
            "city": "Ho Chi Minh City",
            "fullAddress": "123 Le Loi, Ben Nghe, District 1, HCMC",
            "fullName": "Minhaza",
            "estimatedPrice": 20.00,
            "serviceName": "Cleaning"
        },
        {
            "bookingId": 9,
            "requestId": 12,
            "userId": 1,
            "serviceId": 1,
            "addressId": 1,
            "status": "InProgress",
            "scheduledStartTime": "2025-07-27T17:07:00",
            "scheduledEndTime": "2025-07-27T20:52:00",
            "latitude": null,
            "longitude": null,
            "ward": "Ben Nghe",
            "district": "District 1",
            "city": "Ho Chi Minh City",
            "fullAddress": "123 Le Loi, Ben Nghe, District 1, HCMC",
            "fullName": "Minhaza",
            "estimatedPrice": 40.00,
            "serviceName": "Cleaning"
        }
    ],
    "timestamp": "2025-07-22T18:44:46.7975546Z",
    "requestId": "0HNE98NEOGMNO:00000001"
}


{{baseUrl}}/api/Bookings/ActiveByHelper/:helperId

response:
{
    "success": true,
    "statusCode": 200,
    "message": "Thành công",
    "data": [
        {
            "bookingId": 6,
            "requestId": 9,
            "userId": 1,
            "serviceId": 1,
            "addressId": 1,
            "status": "Completed",
            "scheduledStartTime": "2025-07-25T15:02:00",
            "scheduledEndTime": "2025-07-25T17:02:00",
            "latitude": null,
            "longitude": null,
            "ward": "Ben Nghe",
            "district": "District 1",
            "city": "Ho Chi Minh City",
            "fullAddress": "123 Le Loi, Ben Nghe, District 1, HCMC",
            "fullName": "Minhaza",
            "estimatedPrice": 20.00,
            "serviceName": "Cleaning"
        }
    ],
    "timestamp": "2025-07-22T18:45:40.3599948Z",
    "requestId": "0HNE98NEOGMNO:00000002"
}

{{baseUrl}}/api/Bookings/6/status

body:
{"actualEndTime":"2025-07-23T00:44:48","actualStartTime":"2025-07-23T00:44:48","bookingId":6,"helperId":7,"status":"Completed"}

response:
{
    "success": true,
    "statusCode": 200,
    "message": "Thành công",
    "data": {
        "bookingId": 6,
        "userId": 1,
        "serviceId": 1,
        "requestId": 9,
        "helperId": 7,
        "scheduledStartTime": "2025-07-25T15:02:00",
        "scheduledEndTime": "2025-07-25T17:02:00",
        "actualStartTime": "2025-07-23T00:58:51",
        "actualEndTime": "2025-07-23T00:44:48",
        "status": "Completed",
        "cancellationReason": null,
        "cancelledBy": null,
        "cancellationTime": null,
        "freeCancellationDeadline": null,
        "estimatedPrice": 20.00,
        "finalPrice": null,
        "bookingCreationTime": "2025-07-22T08:06:48.4793919"
    },
    "timestamp": "2025-07-22T18:46:56.1365036Z",
    "requestId": "0HNE98NEOGMNP:00000001"
}