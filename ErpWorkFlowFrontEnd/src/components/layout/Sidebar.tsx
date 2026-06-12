import {
  Drawer,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from '@mui/material';
import DashboardIcon from '@mui/icons-material/Dashboard';
import ReceiptIcon from '@mui/icons-material/Receipt';
import AccountTreeIcon from '@mui/icons-material/AccountTree';
import BusinessIcon from '@mui/icons-material/Business';
import PeopleIcon from '@mui/icons-material/People';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const drawerWidth = 240;

const navItems = [
  { label: 'Dashboard', path: '/dashboard', icon: <DashboardIcon /> },
  { label: 'Billing Documents', path: '/billing-documents', icon: <ReceiptIcon /> },
  { label: 'Workflow Management', path: '/workflows', icon: <AccountTreeIcon /> },
  { label: 'Department Management', path: '/departments', icon: <BusinessIcon /> },
  { label: 'User Management', path: '/users', icon: <PeopleIcon />, adminOnly: true },
];

export function Sidebar() {
  const { isAdmin } = useAuth();

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        '& .MuiDrawer-paper': { width: drawerWidth, boxSizing: 'border-box' },
      }}
    >
      <List>
        {navItems
          .filter((item) => !item.adminOnly || isAdmin)
          .map((item) => (
            <ListItemButton
              key={item.path}
              component={NavLink}
              to={item.path}
              sx={{
                '&.active': {
                  backgroundColor: 'action.selected',
                },
              }}
            >
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={item.label} />
            </ListItemButton>
          ))}
      </List>
    </Drawer>
  );
}

export const SIDEBAR_WIDTH = drawerWidth;
