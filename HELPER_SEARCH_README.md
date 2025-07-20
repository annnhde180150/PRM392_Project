# Helper Search Functionality

This document describes the helper search functionality that allows users to find helpers based on service selection.

## Overview

The helper search feature includes:
- Service selection with horizontal scrollable list
- Helper search results with pagination
- Location-based search (within specified radius)
- Helper details with ratings, experience, and availability
- Contact functionality

## API Endpoint

The helper search uses the following API endpoint:
```
GET /api/Helper/Search
```

### Parameters:
- `serviceId` (int): The ID of the selected service
- `latitude` (double): User's current latitude
- `longitude` (double): User's current longitude  
- `radius` (double): Search radius in kilometers
- `page` (int): Page number for pagination
- `pageSize` (int): Number of results per page

### Response Format:
```json
{
    "success": true,
    "statusCode": 200,
    "message": "Thành công",
    "data": [
        {
            "helperId": 1,
            "fullName": "John Doe",
            "profilePictureUrl": "https://example.com/photo.jpg",
            "bio": "Experienced helper with 5 years of experience",
            "averageRating": 4.5,
            "totalHoursWorked": 120.5,
            "serviceName": "Plumber",
            "basePrice": 25.0,
            "priceUnit": "hour",
            "isAvailable": true,
            "distance": 2.5
        }
    ],
    "timestamp": "2025-07-20T06:40:52.0024434Z",
    "requestId": "0HNE7B0MRQ3EN:00000005"
}
```

## Components

### 1. HelperSearchActivity
Main activity that handles:
- Service selection
- Helper search
- Location management
- Pagination

### 2. ServiceSelectionAdapter
Adapter for displaying services in a horizontal scrollable list with selection state.

### 3. HelperSearchAdapter  
Adapter for displaying helper search results with:
- Profile picture
- Name and service
- Bio
- Rating and experience
- Price and distance
- Availability indicator
- Contact button

### 4. HelperSearchResponse
Data model for helper search results with utility methods for formatting.

## Usage

### Starting Helper Search
```java
// Navigate to helper search activity
NavigationHelper.navigateToHelperSearch(context);
```

### Direct Intent
```java
Intent intent = new Intent(context, HelperSearchActivity.class);
startActivity(intent);
```

## Features

### Service Selection
- Horizontal scrollable list of available services
- Visual selection state with purple background
- Auto-selects first service on load

### Location-Based Search
- Uses device GPS or network location
- Configurable search radius (default: 10km)
- Falls back to default location if GPS unavailable

### Helper Results
- Card-based layout with helper information
- Rating display with star rating
- Experience hours worked
- Distance from user location
- Availability status indicator
- Contact button for available helpers

### Pagination
- Loads 20 helpers per page
- Automatic pagination on scroll
- Pull-to-refresh functionality

## UI Design

The design follows the example view provided:
- Purple color scheme matching the app theme
- Service selection similar to the category view
- Helper cards with profile pictures and information
- Modern Material Design components

## Permissions Required

- `ACCESS_FINE_LOCATION`: For precise GPS location
- `ACCESS_COARSE_LOCATION`: For network-based location
- `INTERNET`: For API calls
- `ACCESS_NETWORK_STATE`: For network connectivity checks

## Error Handling

- Network connectivity checks
- Location permission handling
- API error responses
- Empty state display
- Loading states

## Customization

### Colors
Update colors in `colors.xml`:
- `colorPrimary`: Main purple color
- `colorPrimaryDark`: Darker purple for selection
- `colorPrimaryLight`: Light purple for backgrounds

### Search Radius
Modify `DEFAULT_RADIUS` in `HelperSearchActivity`:
```java
private static final double DEFAULT_RADIUS = 10.0; // 10km radius
```

### Page Size
Modify `PAGE_SIZE` in `HelperSearchActivity`:
```java
private static final int PAGE_SIZE = 20;
```

## Integration

To integrate helper search into your app:

1. Add the activities to your navigation
2. Ensure location permissions are granted
3. Test with your API endpoint
4. Customize the UI as needed

## Example Integration

Add to your main menu or dashboard:
```java
Button btnFindHelpers = findViewById(R.id.btnFindHelpers);
btnFindHelpers.setOnClickListener(v -> {
    NavigationHelper.navigateToHelperSearch(this);
});
``` 