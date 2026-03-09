import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <AppBar position="sticky">
      <Toolbar>
        <Typography
          variant="h6"
          component={RouterLink}
          to="/"
          sx={{ flexGrow: 1, textDecoration: 'none', color: 'inherit', fontWeight: 700 }}
        >
          WhosUp
        </Typography>

        {user ? (
          <Box display="flex" alignItems="center" gap={1}>
            <Button color="inherit" component={RouterLink} to="/activities/new">
              Post Activity
            </Button>
            <Button color="inherit" component={RouterLink} to="/my-activities">
              My Activities
            </Button>
            <Typography variant="body2" sx={{ mx: 1 }}>
              {user.displayName}
            </Typography>
            <Button color="inherit" onClick={handleLogout}>
              Logout
            </Button>
          </Box>
        ) : (
          <Box display="flex" gap={1}>
            <Button color="inherit" component={RouterLink} to="/login">
              Login
            </Button>
            <Button color="inherit" variant="outlined" component={RouterLink} to="/register">
              Sign Up
            </Button>
          </Box>
        )}
      </Toolbar>
    </AppBar>
  );
}
