import { Backdrop, CircularProgress } from '@mui/material';
import { useIsFetching, useIsMutating } from '@tanstack/react-query';

export function LoadingOverlay() {
  const isFetching = useIsFetching();
  const isMutating = useIsMutating();
  const open = isFetching > 0 || isMutating > 0;

  return (
    <Backdrop open={open} sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 2 }}>
      <CircularProgress color="inherit" />
    </Backdrop>
  );
}
