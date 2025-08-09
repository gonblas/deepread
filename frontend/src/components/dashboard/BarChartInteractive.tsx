"use client"

import * as React from "react"
import { Bar, BarChart, CartesianGrid, XAxis } from "recharts"

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart"

export const description = "An interactive bar chart"

const chartData = [
  { date: "2024-04-01", attempts: 222, averageScore: 150 },
  { date: "2024-04-02", attempts: 97, averageScore: 180 },
  { date: "2024-04-03", attempts: 167, averageScore: 120 },
  { date: "2024-04-04", attempts: 242, averageScore: 260 },
  { date: "2024-04-05", attempts: 373, averageScore: 290 },
  { date: "2024-04-06", attempts: 301, averageScore: 340 },
  { date: "2024-04-07", attempts: 245, averageScore: 180 },
  { date: "2024-04-08", attempts: 409, averageScore: 320 },
  { date: "2024-04-09", attempts: 59, averageScore: 110 },
  { date: "2024-04-10", attempts: 261, averageScore: 190 },
  { date: "2024-04-11", attempts: 327, averageScore: 350 },
  { date: "2024-04-12", attempts: 292, averageScore: 210 },
  { date: "2024-04-13", attempts: 342, averageScore: 380 },
  { date: "2024-04-14", attempts: 137, averageScore: 220 },
  { date: "2024-04-15", attempts: 120, averageScore: 170 },
  { date: "2024-04-16", attempts: 138, averageScore: 190 },
  { date: "2024-04-17", attempts: 446, averageScore: 360 },
  { date: "2024-04-18", attempts: 364, averageScore: 410 },
  { date: "2024-04-19", attempts: 243, averageScore: 180 },
  { date: "2024-04-20", attempts: 89, averageScore: 150 },
  { date: "2024-04-21", attempts: 137, averageScore: 200 },
  { date: "2024-04-22", attempts: 224, averageScore: 170 },
  { date: "2024-04-23", attempts: 138, averageScore: 230 },
  { date: "2024-04-24", attempts: 387, averageScore: 290 },
  { date: "2024-04-25", attempts: 215, averageScore: 250 },
  { date: "2024-04-26", attempts: 75, averageScore: 130 },
  { date: "2024-04-27", attempts: 383, averageScore: 420 },
  { date: "2024-04-28", attempts: 122, averageScore: 180 },
  { date: "2024-04-29", attempts: 315, averageScore: 240 },
  { date: "2024-04-30", attempts: 454, averageScore: 380 },
  { date: "2024-05-01", attempts: 165, averageScore: 220 },
  { date: "2024-05-02", attempts: 293, averageScore: 310 },
  { date: "2024-05-03", attempts: 247, averageScore: 190 },
  { date: "2024-05-04", attempts: 385, averageScore: 420 },
  { date: "2024-05-05", attempts: 481, averageScore: 390 },
  { date: "2024-05-06", attempts: 498, averageScore: 520 },
  { date: "2024-05-07", attempts: 388, averageScore: 300 },
  { date: "2024-05-08", attempts: 149, averageScore: 210 },
  { date: "2024-05-09", attempts: 227, averageScore: 180 },
  { date: "2024-05-10", attempts: 293, averageScore: 330 },
  { date: "2024-05-11", attempts: 335, averageScore: 270 },
  { date: "2024-05-12", attempts: 197, averageScore: 240 },
  { date: "2024-05-13", attempts: 197, averageScore: 160 },
  { date: "2024-05-14", attempts: 448, averageScore: 490 },
  { date: "2024-05-15", attempts: 473, averageScore: 380 },
  { date: "2024-05-16", attempts: 338, averageScore: 400 },
  { date: "2024-05-17", attempts: 499, averageScore: 420 },
  { date: "2024-05-18", attempts: 315, averageScore: 350 },
  { date: "2024-05-19", attempts: 235, averageScore: 180 },
  { date: "2024-05-20", attempts: 177, averageScore: 230 },
  { date: "2024-05-21", attempts: 82, averageScore: 140 },
  { date: "2024-05-22", attempts: 81, averageScore: 120 },
  { date: "2024-05-23", attempts: 252, averageScore: 290 },
  { date: "2024-05-24", attempts: 294, averageScore: 220 },
  { date: "2024-05-25", attempts: 201, averageScore: 250 },
  { date: "2024-05-26", attempts: 213, averageScore: 170 },
  { date: "2024-05-27", attempts: 420, averageScore: 460 },
  { date: "2024-05-28", attempts: 233, averageScore: 190 },
  { date: "2024-05-29", attempts: 78, averageScore: 130 },
  { date: "2024-05-30", attempts: 340, averageScore: 280 },
  { date: "2024-05-31", attempts: 178, averageScore: 230 },
  { date: "2024-06-01", attempts: 178, averageScore: 200 },
  { date: "2024-06-02", attempts: 470, averageScore: 410 },
  { date: "2024-06-03", attempts: 103, averageScore: 160 },
  { date: "2024-06-04", attempts: 439, averageScore: 380 },
  { date: "2024-06-05", attempts: 88, averageScore: 140 },
  { date: "2024-06-06", attempts: 294, averageScore: 250 },
  { date: "2024-06-07", attempts: 323, averageScore: 370 },
  { date: "2024-06-08", attempts: 385, averageScore: 320 },
  { date: "2024-06-09", attempts: 438, averageScore: 480 },
  { date: "2024-06-10", attempts: 155, averageScore: 200 },
  { date: "2024-06-11", attempts: 92, averageScore: 150 },
  { date: "2024-06-12", attempts: 492, averageScore: 420 },
  { date: "2024-06-13", attempts: 81, averageScore: 130 },
  { date: "2024-06-14", attempts: 426, averageScore: 380 },
  { date: "2024-06-15", attempts: 307, averageScore: 350 },
  { date: "2024-06-16", attempts: 371, averageScore: 310 },
  { date: "2024-06-17", attempts: 475, averageScore: 520 },
  { date: "2024-06-18", attempts: 107, averageScore: 170 },
  { date: "2024-06-19", attempts: 341, averageScore: 290 },
  { date: "2024-06-20", attempts: 408, averageScore: 450 },
  { date: "2024-06-21", attempts: 169, averageScore: 210 },
  { date: "2024-06-22", attempts: 317, averageScore: 270 },
  { date: "2024-06-23", attempts: 480, averageScore: 530 },
  { date: "2024-06-24", attempts: 132, averageScore: 180 },
  { date: "2024-06-25", attempts: 141, averageScore: 190 },
  { date: "2024-06-26", attempts: 434, averageScore: 380 },
  { date: "2024-06-27", attempts: 448, averageScore: 490 },
  { date: "2024-06-28", attempts: 149, averageScore: 200 },
  { date: "2024-06-29", attempts: 103, averageScore: 160 },
  { date: "2024-06-30", attempts: 446, averageScore: 400 },
]

const chartConfig = {
  views: {
    label: "Page Views",
  },
  attempts: {
    label: "Attempts",
    color: "var(--chart-5)",
  },
  averageScore: {
    label: "Average Score",
    color: "var(--chart-1)",
  },
} satisfies ChartConfig

export function BarChartInteractive() {
  const [activeChart, setActiveChart] =
    React.useState<keyof typeof chartConfig>("attempts")

  const total = React.useMemo(
    () => ({
      attempts: chartData.reduce((acc, curr) => acc + curr.attempts, 0),
      averageScore: chartData.reduce((acc, curr) => acc + curr.averageScore, 0),
    }),
    []
  )

  return (
    <Card className="py-0">
      <CardHeader className="flex flex-col items-stretch border-b !p-0 sm:flex-row">
        <div className="flex flex-1 flex-col justify-center gap-1 px-6 pt-4 pb-3 sm:!py-0">
          <CardTitle>Attempts Stats by Day</CardTitle>
          <CardDescription>
            Showing total visitors for the last 3 months
          </CardDescription>
        </div>
        <div className="flex">
          {["attempts", "averageScore"].map((key) => {
            const chart = key as keyof typeof chartConfig
            return (
              <button
                key={chart}
                data-active={activeChart === chart}
                className="data-[active=true]:bg-muted/50 relative z-30 flex flex-1 flex-col justify-center gap-1 border-t px-6 py-4 text-left even:border-l sm:border-t-0 sm:border-l sm:px-8 sm:py-6"
                onClick={() => setActiveChart(chart)}
              >
                <span className="text-muted-foreground text-xs">
                  {chartConfig[chart].label}
                </span>
                <span className="text-lg leading-none font-bold sm:text-3xl">
                  {total[key as keyof typeof total].toLocaleString()}
                </span>
              </button>
            )
          })}
        </div>
      </CardHeader>
      <CardContent className="px-2 sm:p-6">
        <ChartContainer
          config={chartConfig}
          className="aspect-auto h-[250px] w-full"
        >
          <BarChart
            accessibilityLayer
            data={chartData}
            margin={{
              left: 12,
              right: 12,
            }}
          >
            <CartesianGrid vertical={false} />
            <XAxis
              dataKey="date"
              tickLine={false}
              axisLine={false}
              tickMargin={8}
              minTickGap={32}
              tickFormatter={(value) => {
                const date = new Date(value)
                return date.toLocaleDateString("en-US", {
                  month: "short",
                  day: "numeric",
                })
              }}
            />
            <ChartTooltip
              content={
                <ChartTooltipContent
                  className="w-[150px]"
                  nameKey="views"
                  labelFormatter={(value) => {
                    return new Date(value).toLocaleDateString("en-US", {
                      month: "short",
                      day: "numeric",
                      year: "numeric",
                    })
                  }}
                />
              }
            />
            <Bar dataKey={activeChart} fill={`var(--color-${activeChart})`} />
          </BarChart>
        </ChartContainer>
      </CardContent>
    </Card>
  )
}
