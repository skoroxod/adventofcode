using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using NUnit.Framework;

namespace AdventOfCode2017
{
    public class V
    {
        public V(double x, double y, double z)
        {
            X = x;
            Y = y;
            Z = z;
        }

        public override string ToString()
        {
            return $"{nameof(X)}: {X}, {nameof(Y)}: {Y}, {nameof(Z)}: {Z}";
        }

        public static V operator +(V a, V b) => new V(a.X + b.X, a.Y + b.Y, a.Z + b.Z);
        public static V operator *(V a, double b) => new V(a.X*b, a.Y*b, a.Z *b);
        public static V operator *(double b, V a) => new V(a.X*b, a.Y*b, a.Z *b);

        public double X, Y, Z;
        public double Len => Math.Abs(X) + Math.Abs(Y) + Math.Abs(Z);
    }
    public class Point
    {
        public Point(V p, V v, V a)
        {
            P = p;
            V = v;
            A = a;
        }

        public V P, V, A;
    }
    
    [TestFixture]
    public static class Problem
    {
        [Test]
        public static void RunSolve()
        {
            Solve(InputLines);
        }

        public static void Solve(string[] lines)
        {
            var points = lines.Select(line => line.Split(new[] { '<', '>', ',' }, StringSplitOptions.RemoveEmptyEntries).Select(
                    s => ParseVerbose(s)).ToArray())
                .Select(ps => new Point(new V(ps[1], ps[2], ps[3]), new V(ps[5], ps[6], ps[7]), new V(ps[9], ps[10], ps[11]))).ToList();

            double t = 1000;
            var sim = points.Select(p => p.P + t * p.V + p.A * (t * t / 2.0)).ToList();
            int bestI = 0;
            for (int i = 0; i < sim.Count; i++)
                if (sim[i].Len < sim[bestI].Len) bestI = i;
            Console.WriteLine(bestI);
            Console.WriteLine(sim[bestI].Len);

            for (int time = 0; time < 1000; time++)
            {
                points = points.Select(Simulate).ToList();
                points = points.GroupBy(p => p.P.ToString()).Where(g => g.Count() == 1).Select(g => g.First()).ToList();
            }
            Console.WriteLine(points.Count);
        }

        private static Point Simulate(Point p)
        {
            var v2 = p.V + p.A;
            var p2 = p.P + v2;
            return new Point(p2, v2, p.A);
        }

        private static int ParseVerbose(string s)
        {
            try
            {
                return int.Parse(s);

            }
            catch (Exception e)
            {
                return -100500;
            }
        }

        private static string Input => File.ReadAllText(TestContext.CurrentContext.TestDirectory + "\\input.txt");

        private static int[] InputInts => Input.ToInts();

        private static int[][] InputTable => Input.ToIntsTable();

        //static int[] InputLines => File.ReadAllLines(TestContext.CurrentContext.TestDirectory + "\\input.txt").Select(int.Parse).ToArray();

        private static string[] InputLines =>
            File.ReadAllLines(TestContext.CurrentContext.TestDirectory + "\\input.txt");

        public static int[] ToInts(this string text)
        {
            return text.Split(',').Select(int.Parse).ToArray();
        }

        public static int[][] ToIntsTable(this string text)
        {
            return text.ToLines().Select(line => line.Split().Select(int.Parse).ToArray()).ToArray();
        }

        public static string[] ToLines(this string text)
        {
            return text.Split(new[] { "\r", "\n" }, StringSplitOptions.RemoveEmptyEntries);
        }
    }
}