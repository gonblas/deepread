import AppLayout from "@/components/AppLayout"
import { SectionCards } from "@/components/dashboard/SectionCards"
import { BarChartInteractive } from "@/components/dashboard/BarChartInteractive"
import { RecentAttemptsTable } from "@/components/dashboard/RecentAttemptsTable"

const stats = {
  totalAttempts: 12,
  totalQuizzesAttempted: 8,
  averageScore: 76.5,
  bestScore: 98,
  worstScore: 45,
  averageTimeSeconds: 120,
  dailyStatsTimeline: [
    { date: "2025-08-01", attempts: 2, averageScore: 80 },
    { date: "2025-08-02", attempts: 3, averageScore: 75 },
    { date: "2025-08-03", attempts: 1, averageScore: 90 },
  ],
};

function HomePage() {
  return (
    <AppLayout>
      <div className="flex flex-1 flex-col">
          <div className="@container/main flex flex-1 flex-col gap-2">
            <div className="flex flex-col gap-4 py-4 md:gap-6 md:py-6 px-4">
              <SectionCards stats={stats} />
              <BarChartInteractive /> 
              <RecentAttemptsTable />
            </div>
          </div>
      </div>
    </AppLayout>
  )
}

export default HomePage