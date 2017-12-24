using System;
using System.Collections.Generic;
using System.Collections.Immutable;
using System.IO;
using System.Linq;
using NUnit.Framework;

namespace AdventOfCode2017
{
    [TestFixture]
    public static class Problem
    {
        public static void Main(){}
        [Test]
        public static void RunSolve()
        {
            //Solve(3);
            Solve(328);
        }

        public static void Solve(int steps)
        {
            var a = ImmutableList.Create<int>().Add(0);
            var cur = 0;
            for (int i = 1; i <= 2017; i++)
            {
                cur = (cur + steps) % a.Count + 1;
                a = a.Insert(cur, i);
            }
            Console.WriteLine(a[(cur+1)%a.Count]);
            cur = 0;
            for (int i = 1; i <= 50000000; i++)
            {
                cur = (cur + steps) % a.Count + 1;
                a = a.Insert(cur, i);
            }
            var index = a.IndexOf(0);
            Console.WriteLine(a[(index+1)%a.Count]);
        }
    }
}