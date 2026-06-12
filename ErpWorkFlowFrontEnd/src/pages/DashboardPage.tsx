import { useQuery } from '@tanstack/react-query';
import { Box, Card, CardContent, Grid, Typography } from '@mui/material';
import { toast } from 'react-toastify';
import { dashboardApi, queryKeys } from '../api/services';
import { getErrorMessage } from '../api/axios';

const cards = [
  { key: 'totalInProgress', title: 'Total In Progress' },
  { key: 'totalCompleted', title: 'Total Completed' },
  { key: 'myPendingActions', title: 'My Pending Actions' },
  { key: 'myCompletedActions', title: 'My Completed Actions' },
] as const;

export function DashboardPage() {
  const { data } = useQuery({
    queryKey: queryKeys.dashboard,
    queryFn: async () => {
      try {
        const response = await dashboardApi.getSummary();
        return response.data;
      } catch (error) {
        toast.error(getErrorMessage(error, 'Failed to load dashboard data'));
        throw error;
      }
    },
  });

  return (
    <Box>
      <Typography variant="h5" gutterBottom>
        Dashboard
      </Typography>
      <Grid container spacing={3}>
        {cards.map((card) => (
          <Grid key={card.key} item xs={12} sm={6} md={3}>
            <Card>
              <CardContent>
                <Typography color="text.secondary" gutterBottom>
                  {card.title}
                </Typography>
                <Typography variant="h4">{data?.[card.key] ?? 0}</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
