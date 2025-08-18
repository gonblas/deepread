import { SectionCards } from "@/components/statistics/SectionCards";
import { BarChartInteractive } from "@/components/statistics/BarChartInteractive";
import { RecentAttemptsTable } from "@/components/statistics/RecentAttemptsTable";
import {
  StatisticsProvider,
  useStatistics,
} from "@/contexts/statisticsContext";
import { useEffect } from "react";
import { useAuth } from "@/contexts/authContext";
import { SectionHeader } from "@/components/SectionHeader";
import { useNotification } from "@/contexts/notificationContext";
import {
  RecentAttemptsProvider,
  useRecentAttempts,
} from "@/contexts/recentAttemptsContext";


function HomeComponent() {
  const { fetchUserStats, userStats, loading, error } = useStatistics();
  const { user } = useAuth();
  const {
    userAttempts,
    fetchUserRecentAttempts,
    loading: loadingRecentAttempts,
    error: errorRecentAttempts,
  } = useRecentAttempts();
  const { showError } = useNotification();

  useEffect(() => {
    if (user) {
      fetchUserStats();
      fetchUserRecentAttempts();
    }
  }, [user]);

  return (
    <>
      <SectionHeader
        title="Dashboard"
        loading={loading}
        error={error}
        description="Overview of your book statistics and recent activity"
      />
      {loading && <div className="p-4">Loading statistics...</div>}
      {error && showError(error)}
      {!loading && !error && (
        <>
          <SectionCards stats={userStats.stats} />
          <BarChartInteractive chartData={userStats.dailyStatsTimeline} />
          <RecentAttemptsTable recentAttempts={userAttempts} loading={loadingRecentAttempts} error={errorRecentAttempts} />
        </>
      )}
    </>
  );
}

function HomePage() {
  return (
    <StatisticsProvider>
      <RecentAttemptsProvider>
        <HomeComponent />
      </RecentAttemptsProvider>
    </StatisticsProvider>
  );
}

export default HomePage;
