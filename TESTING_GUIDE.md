# 🚀 TNPSC App - Quick Start & Testing Guide

## Installation & Setup

### Prerequisites
- Node.js 16+ (for frontend)
- Java 17+ (for backend)
- Maven 3.9+
- MySQL 8+ (for database)

### Frontend Setup
```bash
cd frontend
npm install
npm run dev
```
The app will start at `http://localhost:5173` (or next available port).

---

## 📱 Testing Flows

### 1. **Authentication Flow**

#### Test Login:
1. Go to `http://localhost:5173/login`
2. Enter email: `test@example.com` 
3. Enter password: `password123`
4. ✅ Should redirect to `/home` with header showing profile
5. Verify top-right header shows: email + Profile dropdown with Logout

#### Test Logout:
1. Click on profile button (top-right with avatar)
2. Click **Logout**
3. ✅ Token should be cleared from localStorage
4. ✅ Should redirect to `/login`
5. Try accessing `/practice` → ✅ Should redirect to login

#### Test Protected Routes:
1. Without login, try `http://localhost:5173/practice` 
2. ✅ Should redirect to `/login`
3. Login, then try `/practice`
4. ✅ Should load practice page

#### Test Token Persistence:
1. Login to the app
2. Refresh the page (F5)
3. ✅ Should still be logged in (token persisted)
4. Check browser DevTools → Application → localStorage for `tnpsc_token`

---

### 2. **Practice Flow - Category Selection**

#### Start Practice Session:
1. Login and click **"Start Practice"** or navigate to `/practice`
2. ✅ Should see "Start Practice" screen
3. ✅ Should show 7 category chips:
   - Indian Polity
   - History
   - Geography
   - Science
   - Economy
   - Current Affairs
   - Mixed Practice
4. Select multiple categories (e.g., History + Geography)
5. Click **"Start Session"**
6. ✅ Should load questions from selected categories
7. ✅ Questions should be in mixed difficulty order (easy → medium → hard)

#### Test Mixed Practice (Default):
1. On category screen, click **"Start Session"** without selecting categories
2. ✅ Should default to "Mixed Practice"
3. ✅ Should load all questions in mixed difficulty order

---

### 3. **Question Practice Flow**

#### Interaction:
1. ✅ Question displays with 4 options (A, B, C, D)
2. Click an option
3. ✅ Button should show as "selected" (blue highlight)
4. ✅ Other buttons should be disabled (grayed out)
5. After 180ms, **Explanation Card** should slide in
6. ✅ Explanation shows:
   - Result badge (✔ or ✖)
   - Success/Failure text
   - Correct answer highlighted
   - Full explanation text
   - TNPSC tip (if available)

#### Navigation:
1. Click **"Next Question"** button
2. ✅ Smooth transition to next question
3. ✅ Progress bar updates smoothly
4. ✅ Question counter updates (e.g., "Question 3 of 10")

#### Session Completion:
1. On last question, "Next Question" button changes to **"Finish Session"**
2. Click it
3. ✅ Should redirect to `/result` page with summary

---

### 4. **Profile Page**

#### Access Profile:
1. Click profile avatar/name in header
2. Click **"Profile"** from dropdown menu
3. ✅ Should navigate to `/profile`
4. ✅ Should display:
   - User name/email
   - Streak (days)
   - Total Questions Attempted
   - Accuracy %

#### Stats Cards:
- Should be in 3-column layout on desktop
- Should stack to 1 column on mobile
- Values should have gradient color (blue→purple)

---

### 5. **Header & Navigation**

#### Header Appearance:
1. ✅ Should be sticky (stays visible when scrolling)
2. ✅ Brand "TNPSC PULSE" on left (with gradient color)
3. ✅ Language toggle (EN/TA) in center-right
4. ✅ Profile section on right showing email
5. ✅ Header has slight blur effect behind it

#### Profile Dropdown:
1. Click profile button
2. ✅ Dropdown appears below
3. ✅ Shows "Profile" and "Logout" menu items
4. ✅ Click elsewhere to close dropdown
5. ✅ Menu items have hover effects (slight highlight)

---

### 6. **UI/UX Polish**

#### Responsive Design:
- **Desktop (1000px+):** Full layout, 3-column grids
- **Tablet (640px+):** 2-column grids
- **Mobile (< 640px):** 1-column layout, hidden profile name
  
Test by resizing browser or using mobile device emulator (F12 → Toggle Device Emulation)

#### Animations:
- ✅ Explanation card slides in smoothly
- ✅ Progress bar animates when updated
- ✅ Buttons have hover transform (slight upward movement)
- ✅ Page transitions are smooth

#### Dark Theme:
- ✅ Background is dark blue (#090b11)
- ✅ Text is light (#f5f7fb)
- ✅ Cards have subtle borders and transparency
- ✅ No harsh contrasts

---

## 🔧 Backend API Requirements

> **Note:** These endpoints need to be implemented/updated in your Java backend

### 1. Login Endpoint
**POST** `/auth/login`

Request:
```json
{
  "email": "aadmin@123",
  "password": "password"
}
```

Current Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiI..."
}
```

**⚠️ Required Response Format:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiI...",
  "user": {
    "id": "123",
    "name": "Aadmin",
    "email": "aadmin@123",
    "streak": 5,
    "totalAttempted": 42,
    "accuracy": 78
  }
}
```

---

### 2. Register Endpoint
**POST** `/auth/register`

Request:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securepass123"
}
```

**⚠️ Required Response (same as login):**
```json
{
  "token": "...",
  "user": { ... }
}
```

---

### 3. Questions Endpoint - Daily
**GET** `/questions/daily`

**⚠️ Required Fields in Response:**
```json
[
  {
    "id": 1,
    "questionEn": "Who drafted the Indian Constitution?",
    "questionTa": "...",
    "optionAEn": "Dr. B.R. Ambedkar",
    "optionBEn": "Pandit Nehru",
    "optionCEn": "Rajendra Prasad",
    "optionDEn": "Vallabhbhai Patel",
    "optionATa": "...", "optionBTa": "...", "optionCTa": "...", "optionDTa": "...",
    "correctAnswer": "A",
    "explanationEn": "Dr. B.R. Ambedkar was the principal architect...",
    "explanationTa": "...",
    "difficulty": "easy",              // 🆕 NEW
    "category": "Indian Polity",       // 🆕 NEW
    "tnpscTipEn": "Ambedkar = Constitution Father, frequent TNPSC topic", // 🆕 NEW
    "tnpscTipTa": "..."                 // 🆕 NEW
  }
]
```

---

### 4. Filter Questions Endpoint (Optional)
**GET** `/questions/filter?categories=Indian%20Polity,History&difficulty=mixed`

Query Parameters:
- `categories` (optional): Comma-separated category names
- `difficulty` (optional): easy, medium, hard, mixed

Response: Same as `/questions/daily` but filtered

---

### 5. Submit Answer Endpoint
**POST** `/questions/submit`

Request:
```json
{
  "questionId": 1,
  "selectedAnswer": "A"
}
```

Current Response:
```json
{
  "correct": true,
  "explanationEn": "...",
  "explanationTa": "..."
}
```

**⚠️ Required Response:**
```json
{
  "correct": true,
  "correctAnswer": "A",
  "explanationEn": "Dr. B.R. Ambedkar was the principal architect...",
  "explanationTa": "...",
  "tnpscTipEn": "Key TNPSC point: Ambedkar drafted Constitution",  // 🆕 NEW
  "tnpscTipTa": "..."  // 🆕 NEW
}
```

---

### 6. User Profile Endpoint (Optional but Recommended)
**GET** `/auth/me`

Headers:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiI...
```

Response:
```json
{
  "id": "123",
  "name": "Aadmin",
  "email": "aadmin@123",
  "streak": 5,
  "totalAttempted": 42,
  "accuracy": 78.5
}
```

---

## 🐛 Troubleshooting

### Issue: "Cannot find module" errors
**Solution:** Run `npm install` in the frontend directory
```bash
cd frontend && npm install
```

### Issue: CORS errors when calling backend
**Solution:** Add CORS headers in your Spring Boot controller:
```java
@CrossOrigin(origins = "http://localhost:5173")
```

### Issue: Token not persisting after refresh
**Solution:** Check if `tnpsc_token` exists in browser localStorage
- Open DevTools (F12)
- Go to Application → localStorage
- Verify `tnpsc_token` is there

### Issue: Header profile dropdown not showing
**Solution:** Make sure you're logged in and Header component is rendering
- Check if token exists in localStorage
- Verify user object in AuthContext

### Issue: Explanation card not showing
**Solution:** Make sure backend returns all required fields (especially `difficulty`, `category`, `tnpscTipEn/Ta`)

---

## 📊 Database Schema Updates Needed

If not already present, add these fields to your `questions` table:

```sql
ALTER TABLE questions ADD COLUMN difficulty VARCHAR(20) DEFAULT 'medium';
ALTER TABLE questions ADD COLUMN category VARCHAR(100);
ALTER TABLE questions ADD COLUMN tnpsc_tip_en TEXT;
ALTER TABLE questions ADD COLUMN tnpsc_tip_ta TEXT;

-- Create index for faster filtering
CREATE INDEX idx_category_difficulty ON questions(category, difficulty);
```

Add to `users` table if not present:
```sql
ALTER TABLE users ADD COLUMN streak INT DEFAULT 0;
ALTER TABLE users ADD COLUMN total_attempted INT DEFAULT 0;
ALTER TABLE users ADD COLUMN accuracy DECIMAL(5,2) DEFAULT 0;
```

---

## ✅ Checklist Before Deployment

- [ ] Frontend: `npm run build` completes without errors
- [ ] Backend: All endpoints return correct response structure
- [ ] Database: All new fields added and populated
- [ ] CORS: Enabled for frontend URL
- [ ] Token: Persists across refresh
- [ ] Logout: Clears token and redirects correctly
- [ ] Protected routes: Block unauthenticated users
- [ ] Progress bar: Animates smoothly
- [ ] Explanation card: Shows all fields
- [ ] Mobile: Responsive layout works

---

## 🎯 Next Steps

1. **Update Backend APIs** to match response structures above
2. **Add missing database fields** (difficulty, category, tnpscTip, etc.)
3. **Test login flow** end-to-end
4. **Test practice flow** with categories and mixed difficulty
5. **Deploy frontend** (`npm run build` → static hosting)
6. **Monitor performance** and user engagement

---

## 📞 Support

For issues or questions:
1. Check browser console (F12) for error messages
2. Check network tab to see actual API responses
3. Verify backend is running on `http://localhost:8080`
4. Ensure database is accessible and populated

Good luck! 🎉
