# App Testing Guide

## Manual Testing Checklist

### 🔧 Pre-Testing Setup
- [ ] Install the APK on Android device/emulator (API 26+)
- [ ] Ensure demo mode is enabled (`ENABLE_DEMO_DATA = true` in Config.kt)
- [ ] Launch the app

### 🔐 Authentication Testing

#### Initial Launch
- [ ] App launches without crashes
- [ ] Authentication screen displays correctly
- [ ] Phone number input field is functional
- [ ] Role selection buttons (Customer/Seller/Admin) work
- [ ] Demo mode toggle is visible and functional

#### Login Flow
- [ ] Enter phone number (any 10-digit number in demo mode)
- [ ] Select each role and verify login works
- [ ] Verify navigation to appropriate dashboard

### 👤 Customer Module Testing

#### Dashboard
- [ ] Customer dashboard loads successfully
- [ ] "Find Nearby Shops" section displays
- [ ] Demo shops are visible in the list
- [ ] Shop cards show: name, category, distance, rating

#### Shop Interaction
- [ ] Tap on shop cards - should show shop details
- [ ] WhatsApp contact button is visible
- [ ] Product list displays for each shop
- [ ] Search functionality works (if implemented)

#### Navigation
- [ ] Bottom navigation works (if implemented)
- [ ] Back button functionality
- [ ] Logout functionality

### 🏪 Seller Module Testing

#### Dashboard
- [ ] Seller dashboard loads successfully
- [ ] Shop information displays correctly
- [ ] Inventory section shows demo products
- [ ] "Add Product" functionality is accessible

#### Shop Management
- [ ] Shop details are editable
- [ ] QR code generation works
- [ ] Inventory management interface is functional
- [ ] Product addition form works

#### Demo Data Verification
- [ ] Demo shop: "Fresh Mart" displays
- [ ] Demo products: Apples, Bananas, Milk are visible
- [ ] Stock quantities are shown correctly
- [ ] Product prices display properly

### 👨‍💼 Admin Module Testing

#### Dashboard
- [ ] Admin dashboard loads successfully
- [ ] Statistics cards display correctly
- [ ] Demo stats show: 150 users, 45 shops, etc.
- [ ] Pending shops section is visible

#### Admin Operations
- [ ] Pending shop approvals list displays
- [ ] Approve/Reject buttons are functional
- [ ] Statistics update correctly
- [ ] User management interface works

#### Demo Data Verification
- [ ] Total Users: 150
- [ ] Total Customers: 120
- [ ] Total Sellers: 30
- [ ] Total Shops: 45
- [ ] Approved Shops: 35
- [ ] Pending Shops: 8
- [ ] Total Products: 1250
- [ ] Active Products: 1200

### 🔄 Cross-Module Testing

#### Role Switching
- [ ] Logout from one role
- [ ] Login with different role
- [ ] Verify correct dashboard loads
- [ ] Data persistence works correctly

#### Error Handling
- [ ] Network error simulation (disable demo mode)
- [ ] Invalid input handling
- [ ] Loading states display correctly
- [ ] Error messages are user-friendly

### 📱 UI/UX Testing

#### Material 3 Design
- [ ] Consistent color scheme throughout app
- [ ] Proper typography hierarchy
- [ ] Appropriate spacing and padding
- [ ] Material 3 components used correctly

#### Responsiveness
- [ ] App works on different screen sizes
- [ ] Portrait and landscape orientations
- [ ] Touch targets are appropriately sized
- [ ] Scrolling works smoothly

#### Accessibility
- [ ] Text is readable
- [ ] Buttons have appropriate labels
- [ ] Color contrast is sufficient
- [ ] Navigation is intuitive

### 🔧 Technical Testing

#### Performance
- [ ] App launches quickly (< 3 seconds)
- [ ] Smooth navigation between screens
- [ ] No memory leaks or crashes
- [ ] Efficient resource usage

#### Data Management
- [ ] Demo data loads correctly
- [ ] State management works properly
- [ ] Configuration changes handled well
- [ ] Proper error recovery

### 🌐 Backend Integration Testing (Optional)

If backend is available:

#### API Connectivity
- [ ] Set `ENABLE_DEMO_DATA = false`
- [ ] Configure correct backend URLs
- [ ] Test user registration
- [ ] Test authentication flow
- [ ] Verify API responses

#### Real Data Flow
- [ ] Create real user account
- [ ] Register as seller with real shop
- [ ] Add real products to inventory
- [ ] Test admin approval flow
- [ ] Verify data persistence

### 📋 Test Results Template

```
Test Date: ___________
Tester: ___________
Device: ___________
Android Version: ___________

Authentication: ✅ / ❌
Customer Module: ✅ / ❌
Seller Module: ✅ / ❌
Admin Module: ✅ / ❌
UI/UX: ✅ / ❌
Performance: ✅ / ❌

Issues Found:
1. ___________
2. ___________
3. ___________

Overall Rating: ___/10
```

### 🚨 Critical Issues to Watch For

1. **App Crashes**: Any unexpected app termination
2. **Login Failures**: Unable to authenticate with any role
3. **Data Loading Issues**: Demo data not displaying
4. **Navigation Problems**: Unable to move between screens
5. **UI Rendering Issues**: Broken layouts or missing elements

### ✅ Success Criteria

The app passes testing if:
- [ ] All three user roles work correctly
- [ ] Demo data displays properly
- [ ] No critical crashes occur
- [ ] UI is responsive and follows Material 3 guidelines
- [ ] Basic functionality works as expected
- [ ] Configuration switching works (demo/live mode)

### 📝 Notes

- This is a demo/prototype version
- Backend integration requires separate setup
- Some features may be placeholder implementations
- Focus on core functionality and user experience
- Report any issues for future development